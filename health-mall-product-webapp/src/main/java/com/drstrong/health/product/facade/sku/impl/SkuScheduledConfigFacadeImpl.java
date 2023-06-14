package com.drstrong.health.product.facade.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.facade.sku.SkuScheduledConfigFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
}
