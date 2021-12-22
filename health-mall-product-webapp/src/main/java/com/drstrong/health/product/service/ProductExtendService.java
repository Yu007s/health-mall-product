package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.ProductExtendEntity;

import java.util.List;

/**
 * 商品扩展信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:58
 */
public interface ProductExtendService {

	/**
	 * 更新或者保存
	 *
	 * @param entity 入参
	 * @return 状态
	 * @author liuqiuyi
	 * @date 2021/12/13 17:52
	 */
	boolean saveOrUpdate(ProductExtendEntity entity);

	/**
	 * 根据 商品 id 查询商品的扩展信息
	 *
	 * @param productId 商品id
	 * @return 商品信息
	 * @author liuqiuyi
	 * @date 2021/12/13 17:43
	 */
	ProductExtendEntity queryByProductId(Long productId);

	List<ProductExtendEntity> queryListByProductIds(List<Long> productIds);
}
