package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.request.product.AddRevenueRequest;

/**
 * 税收编码
 *
 * @author liuqiuyi
 * @date 2021/12/14 14:57
 */
public interface ProductSkuRevenueService {
	/**
	 * 根据 skuId 或者 skuCode 获取税收编码和税率
	 *
	 * @param skuId   skuId
	 * @param skuCode sku编码
	 * @return 税收编码信息
	 * @author liuqiuyi
	 * @date 2021/12/14 15:19
	 */
	ProductSkuRevenueEntity getSkuRevenue(Long skuId, String skuCode);

	/**
	 * sku 保存税收编码
	 *
	 * @param addRevenueRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/14 14:49
	 */
	void revenueAdd(AddRevenueRequest addRevenueRequest);
}
