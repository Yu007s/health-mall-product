package com.drstrong.health.product.service.incentive.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.incentive.IncentivePolicyConfigMapper;
import com.drstrong.health.product.model.entity.incentive.IncentivePolicyConfigEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.incentive.IncentivePolicyConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liuqiuyi
 * @date 2023/6/7 14:33
 */
@Slf4j
@Service
public class IncentivePolicyConfigServiceImpl extends ServiceImpl<IncentivePolicyConfigMapper, IncentivePolicyConfigEntity> implements IncentivePolicyConfigService {
	/**
	 * 根据店铺 id,目标类型和名称,查询政策配置
	 *
	 * @param storeId
	 * @param goalType
	 * @param earningName
	 * @author liuqiuyi
	 * @date 2023/6/7 14:36
	 */
	@Override
	public IncentivePolicyConfigEntity queryByStoreIdAndGoalType(Long storeId, Integer goalType, String earningName) {
		LambdaQueryWrapper<IncentivePolicyConfigEntity> queryWrapper = new LambdaQueryWrapper<IncentivePolicyConfigEntity>()
				.eq(IncentivePolicyConfigEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(IncentivePolicyConfigEntity::getStoreId, storeId)
				.eq(IncentivePolicyConfigEntity::getConfigGoalType, goalType)
				.eq(IncentivePolicyConfigEntity::getEarningName, earningName);
		return baseMapper.selectOne(queryWrapper);
	}
}
