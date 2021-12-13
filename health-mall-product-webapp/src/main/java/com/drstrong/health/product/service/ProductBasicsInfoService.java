package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.product.ProductManageVO;

/**
 * 商品信息 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:38
 */
public interface ProductBasicsInfoService {

	/**
	 * 根据商品 id 查询商品基础信息
	 *
	 * @param productId 商品 id
	 * @return 商品基础信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:30
	 */
	ProductBasicsInfoEntity queryBasicsInfoByProductId(Long productId);

	/**
	 * 保存或者更新商品信息
	 *
	 * @param saveProductRequest 保存商品
	 * @author liuqiuyi
	 * @date 2021/12/13 15:48
	 */
	void saveOrUpdateProduct(SaveProductRequest saveProductRequest);

	/**
	 * 根据商品 id,查询管理端商品信息
	 *
	 * @param productId 商品 id
	 * @return 商品管理端信息
	 * @author liuqiuyi
	 * @date 2021/12/13 20:26
	 */
	ProductManageVO queryManageProductInfo(Long productId);
}
