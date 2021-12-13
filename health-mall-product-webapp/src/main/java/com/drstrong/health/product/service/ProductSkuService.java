package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.ProductSkuEntity;

import java.util.List;

/**
 * 商品 sku 信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 17:10
 */
public interface ProductSkuService {

	/**
	 * 批量保存 sku 信息
	 *
	 * @param skuEntityList 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:11
	 */
	void batchSave(List<ProductSkuEntity> skuEntityList);

	/**
	 * 根据 商品id 查询 sku 集合
	 *
	 * @param productId 商品 id
	 * @return 商品 sku 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 21:13
	 */
	List<ProductSkuEntity> queryByProductId(Long productId);
}
