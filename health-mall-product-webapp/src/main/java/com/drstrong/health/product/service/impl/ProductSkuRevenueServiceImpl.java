package com.drstrong.health.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.ProductSkuRevenueMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.product.AddRevenueRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.ProductSkuRevenueService;
import com.drstrong.health.product.service.ProductSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 税收编码
 *
 * @author liuqiuyi
 * @date 2021/12/14 14:57
 */
@Service
@Slf4j
public class ProductSkuRevenueServiceImpl extends ServiceImpl<ProductSkuRevenueMapper, ProductSkuRevenueEntity> implements ProductSkuRevenueService {
	@Resource
	ProductSkuRevenueMapper productSkuRevenueMapper;

	@Resource
	ProductSkuService productSkuService;

	/**
	 * 根据 skuId 或者 skuCode 获取税收编码和税率
	 *
	 * @author liuqiuyi
	 * @date 2021/12/14 15:19
	 */
	@Override
	public ProductSkuRevenueEntity getSkuRevenue(Long skuId, String skuCode) {
		if (Objects.isNull(skuId) && Objects.isNull(skuCode)) {
			return new ProductSkuRevenueEntity();
		}
		LambdaQueryWrapper<ProductSkuRevenueEntity> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(ProductSkuRevenueEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode());
		if (Objects.nonNull(skuId)) {
			queryWrapper.eq(ProductSkuRevenueEntity::getSkuId, skuId);
		}
		if (Objects.nonNull(skuCode)) {
			queryWrapper.eq(ProductSkuRevenueEntity::getSkuCode, skuCode);
		}
		return productSkuRevenueMapper.selectOne(queryWrapper);
	}

	/**
	 * sku 保存税收编码
	 *
	 * @param addRevenueRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/14 14:49
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void revenueAdd(AddRevenueRequest addRevenueRequest) {
		// 1.校验 sku 是否存在
		ProductSkuEntity productSkuEntity = productSkuService.queryBySkuIdOrCode(addRevenueRequest.getSkuId(), addRevenueRequest.getSkuCode(), null);
		if (Objects.isNull(productSkuEntity)) {
			throw new BusinessException(ErrorEnums.PRODUCT_NOT_EXIST);
		}
		// 2.保存 or 更新
		ProductSkuRevenueEntity skuRevenueEntity = getSkuRevenue(addRevenueRequest.getSkuId(), addRevenueRequest.getSkuCode());
		skuRevenueEntity.setProductId(productSkuEntity.getProductId());
		skuRevenueEntity.setSkuId(addRevenueRequest.getSkuId());
		skuRevenueEntity.setSkuCode(addRevenueRequest.getSkuCode());
		skuRevenueEntity.setRevenueCode(addRevenueRequest.getRevenueCode());
		skuRevenueEntity.setRevenueRate(addRevenueRequest.getRevenueRate());
		saveOrUpdate(skuRevenueEntity);
	}
}
