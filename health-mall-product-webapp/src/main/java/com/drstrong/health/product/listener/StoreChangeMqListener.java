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
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductSkuService;
import com.drstrong.health.product.service.StoreService;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.redis.utils.RedisUtils;
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

/**
 * 店铺修改 MQ 处理类
 *
 * @author liuqiuyi
 * @date 2021/12/30 13:48
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq-topic-config.store-change-topic}", selectorExpression = "${mq-topic-config.store-change-tag}", consumerGroup = "storeChange-consumer_group")
public class StoreChangeMqListener implements RocketMQListener<StoreChangeEvent> {

	/**
	 * 超时时间:5 分钟
	 */
	public static final int STORE_CHANGE_TIME = 60 * 5;

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
	public void onMessage(StoreChangeEvent storeChangeEvent) {
		log.info("invoke StoreChangeMqListener.onMessage() param:{}", storeChangeEvent);
		if (Objects.isNull(storeChangeEvent.getStoreId()) || StringUtils.isBlank(storeChangeEvent.getOperatorId())) {
			log.error("invoke StoreChangeMqListener.onMessage() param is null{}", storeChangeEvent);
			return;
		}
		String lockKey = RedisKeyUtils.getStoreChangeKey(storeChangeEvent.getStoreId());
		// 先加锁,防止页面重复进行点击
		boolean lockFlag = redisUtils.set(lockKey, storeChangeEvent.getStoreId(), STORE_CHANGE_TIME);
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
	}

	private void doStoreChange(StoreChangeEvent storeChangeEvent) {
		StoreEntity storeEntity = storeService.getByStoreId(storeChangeEvent.getStoreId());
		if (Objects.isNull(storeEntity) || Objects.equals(StoreStatusEnum.DISABLE.getCode(), storeEntity.getStoreStatus())) {
			// 店铺已经被删除或者禁用,判断商品是否需要禁用


		}

	}

	/**
	 * 执行更新操作
	 */
	private void doUpdate(UpOffEnum upOffEnum, String storeName, StoreChangeEvent storeChangeEvent) {
		// 1.查询 spu 表
		QuerySpuRequest querySpuRequest = new QuerySpuRequest();
		querySpuRequest.setStoreId(storeChangeEvent.getStoreId());
		List<ProductBasicsInfoEntity> basicsInfoEntityList = productBasicsInfoService.queryProductByParam(querySpuRequest);
		if (CollectionUtils.isEmpty(basicsInfoEntityList)) {
			log.info("invoke StoreChangeMqListener.onMessage(),product not found,end of run. param:{}", storeChangeEvent);
			return;
		}
		// 判断是否需要更新 spu 表
	/*	basicsInfoEntityList.stream().filter(productBasicsInfoEntity -> {
			if (Objects.nonNull(upOffEnum)) {

			}
		})*/


		// 更新 spu 表的信息
		basicsInfoEntityList.forEach(productBasicsInfoEntity -> {
			productBasicsInfoEntity.setSourceName(storeName);
			productBasicsInfoEntity.setState(upOffEnum.getCode());
			productBasicsInfoEntity.setChangedAt(LocalDateTime.now());
			productBasicsInfoEntity.setChangedBy(storeChangeEvent.getOperatorId());
		});
		productBasicsInfoService.updateBatchById(basicsInfoEntityList, basicsInfoEntityList.size());
		// 2.查询 sku 表
		QuerySkuRequest querySkuRequest = new QuerySkuRequest();
		querySkuRequest.setStoreId(storeChangeEvent.getStoreId());
		List<ProductSkuEntity> productSkuEntityList = productSkuService.querySkuByParam(querySkuRequest);
		if (CollectionUtils.isEmpty(productSkuEntityList)) {
			return;
		}
		// 更新 sku 表信息
		productSkuEntityList.forEach(productSkuEntity -> {
			productSkuEntity.setSourceName(storeName);
			productSkuEntity.setState(upOffEnum.getCode());
			productSkuEntity.setChangedAt(LocalDateTime.now());
			productSkuEntity.setChangedBy(storeChangeEvent.getOperatorId());
		});
		productSkuService.saveOrUpdateBatch(productSkuEntityList, productSkuEntityList.size());
	}
}
