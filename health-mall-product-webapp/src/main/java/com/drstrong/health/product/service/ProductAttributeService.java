package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.ProductAttributeEntity;
import com.drstrong.health.product.model.response.product.ProductPropertyVO;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2021/12/13 16:20
 */
public interface ProductAttributeService {

	/**
	 * 保存关联关系
	 *
	 * @param saveEntityList 保存入参
	 * @author liuqiuyi
	 * @date 2021/12/13 16:21
	 */
	void batchSave(List<ProductAttributeEntity> saveEntityList);

	/**
	 * 根据商品 id 进行逻辑删除
	 *
	 * @author liuqiuyi
	 * @date 2021/12/13 19:42
	 */
	void deletedByProductId(Long productId, Long userId);

	/**
	 * 根据 商品 id 查询关联的属性信息
	 *
	 * @param productId 商品 id
	 * @return 属性信息
	 * @author liuqiuyi
	 * @date 2021/12/13 19:36
	 */
	List<ProductAttributeEntity> queryByProductId(Long productId);


	/**
	 * 根据 spuCode 查询商品属性
	 *
	 * @param spuCode 商品编码
	 * @return 属性集合
	 * @author liuqiuyi
	 * @date 2021/12/17 14:32
	 */
	List<ProductPropertyVO> getPropertyByCode(String spuCode);
}
