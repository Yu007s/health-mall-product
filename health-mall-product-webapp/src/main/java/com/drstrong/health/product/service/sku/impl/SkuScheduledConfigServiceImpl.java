package com.drstrong.health.product.service.sku.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.sku.SkuScheduledConfigMapper;
import com.drstrong.health.product.model.entity.sku.SkuScheduledConfigEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.sku.SkuScheduledConfigService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:21
 */
@Slf4j
@Service
public class SkuScheduledConfigServiceImpl extends ServiceImpl<SkuScheduledConfigMapper, SkuScheduledConfigEntity> implements SkuScheduledConfigService {

	/**
	 * 根据 skuCode 查询
	 *
	 * @param skuCode
	 * @param scheduledStatus
	 * @author liuqiuyi
	 * @date 2023/6/14 16:54
	 */
	@Override
	public SkuScheduledConfigEntity getBySkuCode(String skuCode, Integer scheduledStatus) {
		List<SkuScheduledConfigEntity> scheduledConfigEntityList = listBySkuCode(Sets.newHashSet(skuCode), scheduledStatus);
		return CollectionUtil.isEmpty(scheduledConfigEntityList) ? null : scheduledConfigEntityList.get(0);
	}

	/**
	 * 根据 skuCode 查询
	 *
	 * @param skuCodeList
	 * @author liuqiuyi
	 * @date 2023/6/14 16:54
	 */
	@Override
	public List<SkuScheduledConfigEntity> listBySkuCode(Set<String> skuCodeList, Integer scheduledStatus) {
		if (CollectionUtil.isEmpty(skuCodeList)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<SkuScheduledConfigEntity> queryWrapper = new LambdaQueryWrapper<SkuScheduledConfigEntity>()
				.eq(SkuScheduledConfigEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(Objects.nonNull(scheduledStatus), SkuScheduledConfigEntity::getScheduledStatus, scheduledStatus)
				.in(SkuScheduledConfigEntity::getSkuCode, skuCodeList);
		return baseMapper.selectList(queryWrapper);
	}

	/**
	 * 根据 skuCode 批量更新定时任务执行状态
	 *
	 * @param skuCodeList
	 * @param scheduledStatus
	 * @param operatorId
	 * @author liuqiuyi
	 * @date 2023/6/14 16:25
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchUpdateScheduledStatusByCodes(Set<String> skuCodeList, Integer scheduledStatus, Long operatorId) {
		int size = baseMapper.batchUpdateScheduledStatusByCodes(skuCodeList, scheduledStatus, operatorId);
		if (size > 0) {
			log.info("已将skuCode列表:{} 的状态更新为:{},操作人是:{}", skuCodeList, scheduledStatus, operatorId);
		}
	}
}
