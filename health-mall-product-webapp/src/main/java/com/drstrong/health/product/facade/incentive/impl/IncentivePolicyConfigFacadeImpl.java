package com.drstrong.health.product.facade.incentive.impl;

import com.drstrong.health.product.facade.incentive.IncentivePolicyConfigFacade;
import com.drstrong.health.product.model.entity.incentive.IncentivePolicyConfigEntity;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.incentive.SaveEarningNameRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.incentive.IncentivePolicyConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:41
 */
@Slf4j
@Service
public class IncentivePolicyConfigFacadeImpl implements IncentivePolicyConfigFacade {
	@Resource
	IncentivePolicyConfigService incentivePolicyConfigService;

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
}
