package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.drstrong.health.product.dao.ProductSkuMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.service.ProductSkuService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author liuqiuyi
 * @date 2021/12/13 17:11
 */
@Slf4j
@Service
public class ProductSkuServiceImpl implements ProductSkuService {
	@Resource
	ProductSkuMapper productSkuMapper;

	/**
	 * 批量保存 sku 信息
	 *
	 * @param skuEntityList 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:11
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void batchSave(List<ProductSkuEntity> skuEntityList) {
		if (CollectionUtils.isEmpty(skuEntityList)) {
			return;
		}
		productSkuMapper.batchInsert(skuEntityList);
	}

	/**
	 * 根据 商品id 查询 sku 集合
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	@Override
	public List<ProductSkuEntity> queryByProductId(Long productId) {
		if (Objects.isNull(productId)) {
			return Lists.newArrayList();
		}
		LambdaQueryWrapper<ProductSkuEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuEntity::getProductId, productId).eq(ProductSkuEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		return productSkuMapper.selectList(queryWrapper);
	}
}
