package com.drstrong.health.product.listener;

import com.drstrong.health.product.config.MqTopicConfig;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.productstore.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.StoreStatusEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.mq.model.SkuStateStockMqEvent;
import com.drstrong.health.product.mq.model.product.StoreChangeEvent;
import com.drstrong.health.product.mq.model.product.StoreChangeTypeEnum;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.service.productstore.ProductStoreService;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.product.utils.CustomTLogMqConsumerProcessor;
import com.drstrong.health.product.utils.MqMessageUtil;
import com.drstrong.health.redis.utils.RedisUtils;
import com.google.common.collect.Lists;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 店铺修改 MQ 处理类
 *
 * @author liuqiuyi
 * @date 2021/12/30 13:48
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "rocketMq", name = "enable", havingValue = "true", matchIfMissing = true)
@RocketMQMessageListener(topic = "${store.change.topic}", selectorExpression = "${store.change.tag}", consumerGroup = "${store.change.consumer.group}")
public class StoreChangeMqListener implements RocketMQListener<TLogMqWrapBean<StoreChangeEvent>> {

	/**
	 * 超时时间:3 分钟
	 */
	public static final int STORE_CHANGE_TIME = 60 * 3;

	@Resource
	private RedisUtils redisUtils;

	@Resource
	private ProductStoreService storeService;

	@Resource
	private ProductBasicsInfoService productBasicsInfoService;

	@Resource
	private ProductSkuService productSkuService;

	@Resource
	private MqMessageUtil mqMessageUtil;

	@Resource
	private MqTopicConfig mqTopicConfig;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void onMessage(TLogMqWrapBean<StoreChangeEvent> tLogMqWrapBean) {
		CustomTLogMqConsumerProcessor.process(tLogMqWrapBean, this::handle);
	}

	private void handle(TLogMqWrapBean<StoreChangeEvent> tLogMqWrapBean) {
		StoreChangeEvent storeChangeEvent = tLogMqWrapBean.getT();
		log.info("invoke StoreChangeMqListener.onMessage() param:{}", storeChangeEvent);
		if (Objects.isNull(storeChangeEvent.getStoreId()) || StringUtils.isBlank(storeChangeEvent.getOperatorId())) {
			log.error("invoke StoreChangeMqListener.onMessage() param is null{}", storeChangeEvent);
			return;
		}
		String lockKey = RedisKeyUtils.getStoreChangeKey(storeChangeEvent.getStoreId());
		// 先加锁,防止页面重复进行点击
		boolean lockFlag = redisUtils.setIfAbsent(lockKey, storeChangeEvent.getStoreId(), STORE_CHANGE_TIME);
		if (!lockFlag) {
			log.info("invoke StoreChangeMqListener.onMessage() add lock error,param:{}", storeChangeEvent);
			// 如果加锁失败,抛出异常,进行重试
			throw new BusinessException(ErrorEnums.ADD_LOCK_ERROR);
		}
		try {
			doStoreChange(storeChangeEvent);
		} catch (Exception e) {
			log.error("invoke StoreChangeMqListener.onMessage() an error occurred.param:{}", storeChangeEvent, e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		} finally {
			redisUtils.del(lockKey);
		}
		log.info("invoke StoreChangeMqListener.onMessage() success!!! param:{}", storeChangeEvent);
	}

	private void doStoreChange(StoreChangeEvent storeChangeEvent) {
		// 获取店铺信息
		StoreEntity storeEntity = storeService.getByStoreId(storeChangeEvent.getStoreId());
		if (Objects.equals(StoreChangeTypeEnum.UPDATE_NAME, storeChangeEvent.getStoreChangeTypeEnum())) {
			if (Objects.isNull(storeEntity)) {
				log.error("invoke StoreChangeMqListener.onMessage(),store not found .param:{}", storeChangeEvent);
				return;
			}
			doUpdateStoreName(null, storeEntity.getName(), storeChangeEvent);
		} else if (Objects.equals(StoreChangeTypeEnum.UPDATE_STATE, storeChangeEvent.getStoreChangeTypeEnum())) {
			if (Objects.isNull(storeEntity) || Objects.equals(storeEntity.getStoreStatus(), StoreStatusEnum.DISABLE.getCode())) {
				doUpdateStoreName(UpOffEnum.DOWN, null, storeChangeEvent);
			}
			// 重新上架的情况暂不考虑,因为上架涉及到一系列的判断操作,而且下架前的商品信息还需要保存,后面有这种需求了在补充功能,已同产品确认过
		}
	}

	/**
	 * 执行更新操作
	 */
	private void doUpdateStoreName(UpOffEnum upOffEnum, String storeName, StoreChangeEvent storeChangeEvent) {
		// 1.查询 spu 表
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setStoreId(storeChangeEvent.getStoreId());
		List<ProductBasicsInfoEntity> basicsInfoEntityList = productBasicsInfoService.queryProductByParam(querySpuRequest);
		if (CollectionUtils.isEmpty(basicsInfoEntityList)) {
			log.info("invoke StoreChangeMqListener.onMessage(),product not found,end of run. param:{}", storeChangeEvent);
			return;
		}
		// 判断是否需要更新 spu 表
		basicsInfoEntityList = basicsInfoEntityList.stream()
				.filter(productBasicsInfoEntity -> {
					if (Objects.nonNull(upOffEnum)) {
						return !Objects.equals(upOffEnum.getCode(), productBasicsInfoEntity.getState());
					} else if (StringUtils.isNotBlank(storeName)) {
						return !Objects.equals(storeName, productBasicsInfoEntity.getSourceName());
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(basicsInfoEntityList)) {
			// 更新 spu 表的名称信息
			basicsInfoEntityList.forEach(productBasicsInfoEntity -> {
				if (Objects.nonNull(upOffEnum)) {
					productBasicsInfoEntity.setState(upOffEnum.getCode());
				} else if (StringUtils.isNotBlank(storeName)) {
					productBasicsInfoEntity.setSourceName(storeName);
				}
				productBasicsInfoEntity.setChangedAt(LocalDateTime.now());
				productBasicsInfoEntity.setChangedBy(storeChangeEvent.getOperatorId());
			});
			productBasicsInfoService.updateBatchById(basicsInfoEntityList);
			log.info("invoke StoreChangeMqListener.onMessage(),update spu size:{}", basicsInfoEntityList.size());
		}

		// 2.查询 sku 表
		QuerySkuRequest querySkuRequest = new QuerySkuRequest();
		querySkuRequest.setStoreId(storeChangeEvent.getStoreId());
		List<ProductSkuEntity> productSkuEntityList = productSkuService.querySkuByParam(querySkuRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			return;
		}
		// 判断是否需要更新 sku 表
		productSkuEntityList = productSkuEntityList.stream()
				.filter(productSkuEntity -> {
					if (Objects.nonNull(upOffEnum)) {
						return !Objects.equals(upOffEnum.getCode(), productSkuEntity.getState());
					} else if (StringUtils.isNotBlank(storeName)) {
						return !Objects.equals(storeName, productSkuEntity.getSourceName());
					} else {
						return false;
					}
				})
				.collect(Collectors.toList());
		if (CollectionUtils.isNotEmpty(productSkuEntityList)) {
			List<Long> skuIds = Lists.newArrayListWithExpectedSize(productSkuEntityList.size());
			// 更新 sku 表信息
			productSkuEntityList.forEach(productSkuEntity -> {
				skuIds.add(productSkuEntity.getId());
				if (Objects.nonNull(upOffEnum)) {
					productSkuEntity.setState(upOffEnum.getCode());
				} else if (StringUtils.isNotBlank(storeName)) {
					productSkuEntity.setSourceName(storeName);
				}
				productSkuEntity.setChangedAt(LocalDateTime.now());
				productSkuEntity.setChangedBy(storeChangeEvent.getOperatorId());
			});
			productSkuService.saveOrUpdateBatch(productSkuEntityList, productSkuEntityList.size());
			// 发送 mq 到库存
			sendMsg2Stock(skuIds, upOffEnum, storeChangeEvent.getOperatorId());
			log.info("invoke StoreChangeMqListener.onMessage(),update sku size:{}", productSkuEntityList.size());
		}
	}

	/**
	 * 发送 MQ 到库存,通知 sku 上下架状态
	 */
	private void sendMsg2Stock(List<Long> skuIdList, UpOffEnum upOffEnum, String operatorId) {
		SkuStateStockMqEvent stateStockMqEvent = new SkuStateStockMqEvent();
		stateStockMqEvent.setSkuIdList(skuIdList);
		stateStockMqEvent.setState(upOffEnum.getCode());
		stateStockMqEvent.setUserId(operatorId);
		mqMessageUtil.sendMsg(mqTopicConfig.getSkuStateStockTopic(), mqTopicConfig.getSkuStateStockTag(), stateStockMqEvent);
	}
}
