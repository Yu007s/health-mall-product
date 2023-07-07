package com.drstrong.health.product.service.incentive.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.incentive.SkuIncentivePolicyMapper;
import com.drstrong.health.product.model.entity.incentive.SkuIncentivePolicyEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.incentive.SkuIncentivePolicyService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/7 16:33
 */
@Slf4j
@Service
public class SkuIncentivePolicyServiceImpl extends ServiceImpl<SkuIncentivePolicyMapper, SkuIncentivePolicyEntity> implements SkuIncentivePolicyService {
	/**
	 * 根据 skuCode 查询激励政策
	 *
	 * @param skuCode
	 * @author liuqiuyi
	 * @date 2023/6/7 16:58
	 */
	@Override
	public SkuIncentivePolicyEntity queryBySkuCode(String skuCode) {
		if (StrUtil.isBlank(skuCode)) {
			return null;
		}
		LambdaQueryWrapper<SkuIncentivePolicyEntity> queryWrapper = new LambdaQueryWrapper<SkuIncentivePolicyEntity>()
				.eq(SkuIncentivePolicyEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(SkuIncentivePolicyEntity::getSkuCode, skuCode);
		return baseMapper.selectOne(queryWrapper);
	}

	/**
	 * 根据 skuCode 查询激励政策
	 *
	 * @param skuCodes
	 * @author liuqiuyi
	 * @date 2023/6/13 17:50
	 */
	@Override
	public List<SkuIncentivePolicyEntity> listBySkuCode(Set<String> skuCodes) {
		if (CollectionUtil.isEmpty(skuCodes)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<SkuIncentivePolicyEntity> queryWrapper = new LambdaQueryWrapper<SkuIncentivePolicyEntity>()
				.eq(SkuIncentivePolicyEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.in(SkuIncentivePolicyEntity::getSkuCode, skuCodes);
		return baseMapper.selectList(queryWrapper);
	}
}
