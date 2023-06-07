package com.drstrong.health.product.facade.incentive.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.drstrong.health.common.enums.OperateTypeEnum;
import com.drstrong.health.product.constants.OperationLogConstant;
import com.drstrong.health.product.facade.incentive.SkuIncentivePolicyFacade;
import com.drstrong.health.product.model.OperationLog;
import com.drstrong.health.product.model.entity.incentive.IncentivePolicyConfigEntity;
import com.drstrong.health.product.model.entity.incentive.SkuIncentivePolicyEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.incentive.IncentivePolicyConfigService;
import com.drstrong.health.product.service.incentive.SkuIncentivePolicyService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.drstrong.health.product.utils.OperationLogSendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:41
 */
@Slf4j
@Service
public class SkuIncentivePolicyFacadeImpl implements SkuIncentivePolicyFacade {
	@Resource
	IncentivePolicyConfigService incentivePolicyConfigService;

	@Resource
	SkuIncentivePolicyService skuIncentivePolicyService;

	@Resource
	private OperationLogSendUtil operationLogSendUtil;

	/**
	 * 保存店铺下的收益名称
	 *
	 * @param saveEarningNameRequest
	 * @author liuqiuyi
	 * @date 2023/6/7 11:42
	 */
	@Override
	public void saveEarningName(SaveEarningNameRequest saveEarningNameRequest) {
		// 1.校验店铺下同类型的配置项,名称不能重复
		IncentivePolicyConfigEntity incentivePolicyConfigEntity = incentivePolicyConfigService.queryByStoreIdAndGoalType(saveEarningNameRequest.getStoreId(),
				saveEarningNameRequest.getConfigGoalType(), saveEarningNameRequest.getEarningName());
		if (Objects.nonNull(incentivePolicyConfigEntity)) {
			throw new BusinessException(ErrorEnums.INCENTIVE_POLICY_CONFIG_REPEAT);
		}
		// 2.保存
		IncentivePolicyConfigEntity saveParam = IncentivePolicyConfigEntity.buildDefaultEntity(saveEarningNameRequest.getOperatorId());
		saveParam.setStoreId(saveEarningNameRequest.getStoreId());
		saveParam.setConfigGoalType(saveEarningNameRequest.getConfigGoalType());
		saveParam.setEarningName(saveEarningNameRequest.getEarningName());
		incentivePolicyConfigService.save(saveParam);
	}

	/**
	 * 保存或者更新 sku 的激励政策
	 *
	 * @param saveOrUpdateSkuPolicyRequest
	 * @author liuqiuyi
	 * @date 2023/6/7 16:52
	 */
	@Override
	public void saveOrUpdateSkuPolicy(SaveOrUpdateSkuPolicyRequest saveOrUpdateSkuPolicyRequest) {
		// 1.判断是保存还是更新激励政策
		SkuIncentivePolicyEntity skuIncentivePolicyEntity = skuIncentivePolicyService.queryBySkuCode(saveOrUpdateSkuPolicyRequest.getSkuCode());
		// 记录日志
		OperationLog operationLog = OperationLog.buildOperationLog(saveOrUpdateSkuPolicyRequest.getSkuCode(), OperationLogConstant.MALL_PRODUCT_SKU_CHANGE,
				OperationLogConstant.SKU_INCENTIVE_POLICY_CHANGE, saveOrUpdateSkuPolicyRequest.getOperatorId(), saveOrUpdateSkuPolicyRequest.getOperatorName(),
				OperateTypeEnum.CMS.getCode(), JSONUtil.toJsonStr(skuIncentivePolicyEntity));
		if (Objects.isNull(skuIncentivePolicyEntity)) {
			skuIncentivePolicyEntity = SkuIncentivePolicyEntity.buildDefaultEntity(saveOrUpdateSkuPolicyRequest.getOperatorId());
		}
		skuIncentivePolicyEntity.setStoreId(saveOrUpdateSkuPolicyRequest.getStoreId());
		skuIncentivePolicyEntity.setSkuCode(saveOrUpdateSkuPolicyRequest.getSkuCode());
		skuIncentivePolicyEntity.setCostPrice(BigDecimalUtil.Y2F(saveOrUpdateSkuPolicyRequest.getCostPrice()));
		skuIncentivePolicyEntity.setIncentivePolicyInfo(BeanUtil.copyToList(saveOrUpdateSkuPolicyRequest.getSkuIncentivePolicyList(), SkuIncentivePolicyEntity.IncentivePolicyInfo.class));
		skuIncentivePolicyEntity.setChangedBy(saveOrUpdateSkuPolicyRequest.getOperatorId());
		skuIncentivePolicyEntity.setChangedAt(LocalDateTime.now());
		skuIncentivePolicyService.saveOrUpdate(skuIncentivePolicyEntity);
		// 2.保存操作日志
		operationLogSendUtil.sendOperationLog(operationLog);
	}
}
