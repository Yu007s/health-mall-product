package com.drstrong.health.product.service.sku.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.sku.StoreSkuInfoMapper;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.sku.StoreSkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author liuqiuyi
 * @date 2023/6/8 15:20
 */
@Slf4j
@Service
public class StoreSkuInfoServiceImpl extends ServiceImpl<StoreSkuInfoMapper, StoreSkuInfoEntity> implements StoreSkuInfoService {


	/**
	 * 根据编码查询 sku 信息
	 *
	 * @param skuCode
	 * @author liuqiuyi
	 * @date 2023/6/8 15:22
	 */
	@Override
	public StoreSkuInfoEntity queryBySkuCode(String skuCode) {
		if (StrUtil.isBlank(skuCode)) {
			return null;
		}
		LambdaQueryWrapper<StoreSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<StoreSkuInfoEntity>()
				.eq(StoreSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
				.eq(StoreSkuInfoEntity::getSkuCode, skuCode);
		return baseMapper.selectOne(queryWrapper);
	}
}
