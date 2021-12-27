package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.product.CategoryAttributeItemVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品分类属性 service
 *
 * @author liuqiuyi
 * @date 2021/12/13 14:22
 */
public interface CategoryAttributeItemService {

	/**
	 * 根据 id 集合查询商品关联的属性
	 *
	 * @param attributeIdList 属性 id 集合
	 * @return 关联的属性集合
	 * @author liuqiuyi
	 * @date 2021/12/13 20:57
	 */
	List<CategoryAttributeItemEntity> queryByAttributeIdList(Set<Long> attributeIdList, Long categoryId);

	/**
	 * 根据 id 集合查询商品关联的属性,并转成 map结构
	 *
	 * @param attributeIdList 属性 id 集合
	 * @param categoryId 分类 id
	 * @return 属性的 map 结构 ,key = id,value = 属性值
	 * @author liuqiuyi
	 * @date 2021/12/13 21:08
	 */
	Map<Long, CategoryAttributeItemEntity> queryByIdListToMap(Set<Long> attributeIdList, Long categoryId);


	/**
	 * 查找指定分类的属性项
	 *
	 * @param categoryId 分类 id
	 * @return 商品属性返回值
	 * @author liuqiuyi
	 * @date 2021/12/13 14:34
	 */
	List<CategoryAttributeItemVO> getItems(Long categoryId);
}
