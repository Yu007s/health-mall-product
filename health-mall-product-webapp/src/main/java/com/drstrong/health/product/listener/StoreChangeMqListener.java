package com.drstrong.health.product.listener;

import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.StoreStatusEnum;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.mq.model.product.StoreChangeEvent;
import com.drstrong.health.product.mq.model.product.StoreChangeTypeEnum;
import com.drstrong.health.product.service.product.ProductBasicsInfoService;
import com.drstrong.health.product.service.product.ProductSkuService;
import com.drstrong.health.product.service.store.StoreService;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.product.utils.CustomTLogMqConsumerProcessor;
import com.drstrong.health.redis.utils.RedisUtils;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
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
@RocketMQMessageListener(topic = "${mq-topic-config.store-change-topic}", selectorExpression = "${mq-topic-config.store-change-tag}", consumerGroup = "storeChange-consumer_group")
public class StoreChangeMqListener implements RocketMQListener<TLogMqWrapBean<StoreChangeEvent>> {

	/**
	 * 超时时间:3 分钟
	 */
	public static final int STORE_CHANGE_TIME = 60 * 3;

	@Resource
	private RedisUtils redisUtils;

	@Resource
	private StoreService storeService;

	@Resource
	private ProductBasicsInfoService productBasicsInfoService;

	@Resource
	private ProductSkuService productSkuService;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void onMessage(TLogMqWrapBean<StoreChangeEvent> tLogMqWrapBean) {
		CustomTLogMqConsumerProcessor.process(tLogMqWrapBean,this::handle);
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
			} else {
				doUpdateStoreName(UpOffEnum.UP, null, storeChangeEvent);
			}
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
			// 更新 sku 表信息
			productSkuEntityList.forEach(productSkuEntity -> {
				if (Objects.nonNull(upOffEnum)) {
					productSkuEntity.setState(upOffEnum.getCode());
				} else if (StringUtils.isNotBlank(storeName)) {
					productSkuEntity.setSourceName(storeName);
				}
				productSkuEntity.setChangedAt(LocalDateTime.now());
				productSkuEntity.setChangedBy(storeChangeEvent.getOperatorId());
			});
			productSkuService.saveOrUpdateBatch(productSkuEntityList, productSkuEntityList.size());
			log.info("invoke StoreChangeMqListener.onMessage(),update sku size:{}", productSkuEntityList.size());
		}
	}
}
