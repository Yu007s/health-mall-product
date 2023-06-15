package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:32
 */
@Slf4j
@Service
public class SkuScheduledConfigFacadeImpl implements SkuScheduledConfigFacade {
	@Resource
	SkuScheduledConfigService skuScheduledConfigService;

	@Resource
	OperationLogSendUtil operationLogSendUtil;

	@Resource
	StoreSkuInfoService storeSkuInfoService;

	/**
	 * 批量取消 skuCode 的定时配置
	 *
	 * @param skuCodeList
	 * @param operatorName
	 * @param operatorId
	 * @author liuqiuyi
	 * @date 2023/6/14 16:53
	 */
	@Override
	public void batchUpdateScheduledStatusToCancelByCodes(Set<String> skuCodeList, Long operatorId, String operatorName) {
		log.info("invoke batchUpdateScheduledStatusToCancelByCodes param:{},{},{}", skuCodeList, operatorId, operatorName);
		// 1.根据 skuCode 查询配置
		List<SkuScheduledConfigEntity> skuScheduledConfigEntityList = skuScheduledConfigService.listBySkuCode(skuCodeList, ScheduledStatusEnum.UN_COMPLETE.getCode());
		if (CollectionUtil.isEmpty(skuScheduledConfigEntityList)) {
			log.info("未找到需要取消的sku定时上下架配置,不处理!参数为:{},{},{}", skuCodeList, operatorId, operatorName);
			return;
		}
		// 2.批量取消 skuCode 的定时配置
		Set<String> needCancelSkuCodes = skuScheduledConfigEntityList.stream().map(SkuScheduledConfigEntity::getSkuCode).collect(Collectors.toSet());
		skuScheduledConfigService.batchUpdateScheduledStatusByCodes(needCancelSkuCodes, ScheduledStatusEnum.CANCEL.getCode(), operatorId);
		// 3.记录日志
		sendSkuStatusUpdateLog(skuScheduledConfigEntityList, needCancelSkuCodes, operatorId, operatorName);
	}

	private void sendSkuStatusUpdateLog(List<SkuScheduledConfigEntity> beforeDataList, Set<String> skuCodeList, Long operatorId, String operatorName) {
		Map<String, SkuScheduledConfigEntity> skuCodeInfoMap = skuScheduledConfigService.listBySkuCode(skuCodeList, null)
				.stream().collect(toMap(SkuScheduledConfigEntity::getSkuCode, dto -> dto, (v1, v2) -> v1));
		// 循环发送操作日志
		beforeDataList.forEach(skuScheduledConfigEntity -> {
			OperationLog operationLog = OperationLog.buildOperationLog(skuScheduledConfigEntity.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
					OperationLogConstant.SKU_SCHEDULED_CONFIG_CHANGE, operatorId, operatorName,
					OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(skuScheduledConfigEntity));
			operationLog.setChangeAfterData(JSONUtil.toJsonStr(skuCodeInfoMap.get(skuScheduledConfigEntity.getSkuCode())));
			operationLogSendUtil.sendOperationLog(operationLog);
		});
	}

	/**
	 * 保存或者更新 sku 的上下架状态
	 *
	 * @param scheduledSkuUpDownRequest
	 * @author liuqiuyi
	 * @date 2023/6/15 10:15
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOrUpdateSkuConfig(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
		// 1.查询 skuCode 配置是否存在
		SkuScheduledConfigEntity skuScheduledConfigEntity = skuScheduledConfigService.getBySkuCode(scheduledSkuUpDownRequest.getSkuCode(), null);
		// 判断是否在处理中,如果在处理中,提示稍后再试
		if (Objects.nonNull(skuScheduledConfigEntity) && Objects.equals(skuScheduledConfigEntity.getScheduledStatus(), ScheduledStatusEnum.IN_PROCESS.getCode())) {
			throw new BusinessException(ErrorEnums.SKU_SCHEDULED_IN_PROCESS);
		}
		// 2.组装操作日志
		OperationLog operationLog = OperationLog.buildOperationLog(scheduledSkuUpDownRequest.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
				OperationLogConstant.SKU_SCHEDULED_CONFIG_CHANGE, scheduledSkuUpDownRequest.getOperatorId(), scheduledSkuUpDownRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), null);
		// 3.修改或者保存
		if (Objects.isNull(skuScheduledConfigEntity)) {
			operationLog.setChangeBeforeData(null);
			skuScheduledConfigEntity = SkuScheduledConfigEntity.buildDefaultEntity(scheduledSkuUpDownRequest.getOperatorId());
		} else {
			operationLog.setChangeBeforeData(JSONUtil.toJsonStr(skuScheduledConfigEntity));
			skuScheduledConfigEntity.setChangedAt(LocalDateTime.now());
			skuScheduledConfigEntity.setChangedBy(scheduledSkuUpDownRequest.getOperatorId());
		}
		skuScheduledConfigEntity.setSkuCode(scheduledSkuUpDownRequest.getSkuCode());
		skuScheduledConfigEntity.setScheduledType(scheduledSkuUpDownRequest.getScheduledType());
		skuScheduledConfigEntity.setScheduledDateTime(scheduledSkuUpDownRequest.getScheduledDateTime());
		skuScheduledConfigEntity.setScheduledStatus(ScheduledStatusEnum.UN_COMPLETE.getCode());
		skuScheduledConfigService.saveOrUpdate(skuScheduledConfigEntity);
		// 4.发送操作日志
		SkuScheduledConfigEntity changeAfterDataEntity = skuScheduledConfigService.getBySkuCode(scheduledSkuUpDownRequest.getSkuCode(), null);
		operationLog.setChangeAfterData(JSONUtil.toJsonStr(changeAfterDataEntity));
		operationLogSendUtil.sendOperationLog(operationLog);
		// 5.将 sku 的状态更新为 预约上架或预约下架
		Integer skuStatus = Objects.equals(ScheduledSkuUpDownRequest.SCHEDULED_UP, scheduledSkuUpDownRequest.getScheduledType()) ? UpOffEnum.SCHEDULED_UP.getCode() : UpOffEnum.SCHEDULED_DOWN.getCode();
		storeSkuInfoService.batchUpdateSkuStatusByCodes(Sets.newHashSet(scheduledSkuUpDownRequest.getSkuCode()), skuStatus, scheduledSkuUpDownRequest.getOperatorId());
	}
}
