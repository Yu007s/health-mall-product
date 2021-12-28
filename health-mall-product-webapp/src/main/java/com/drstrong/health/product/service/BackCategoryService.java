package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.enums.CategoryProductNumOperateEnum;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 后台分类 service 接口
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:02
 */
public interface BackCategoryService {

	/**
	 * 校验后台分类是否存在
	 *
	 * @param backCategoryIdList 后台分类 id
	 * @author liuqiuyi
	 * @date 2021/12/26 16:07
	 */
	void checkCategoryIsExist(Set<Long> backCategoryIdList);

	/**
	 * 增加或者减少后台分类 id 的商品数量
	 *
	 * @param categoryId 后台分类id
	 * @param count      要累加的商品数量
	 * @author liuqiuyi
	 * @date 2021/12/28 11:08
	 */
	void addOrReduceProductNumById(Long categoryId, Integer count, CategoryProductNumOperateEnum operateEnum);

	/**
	 * 根据 id 集合,查询后台分类集合
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 后台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<BackCategoryEntity> queryByIdList(Set<Long> categoryIdList);


	/**
	 * 根据后台分类 id,获取商品数量,组装成 map
	 *
	 * @param categoryIdList 后台分类 id
	 * @return map.key = 后台分类 id, map.value = 商品数量
	 * @author liuqiuyi
	 * @date 2021/12/26 16:48
	 */
	Map<Long, Integer> getBackIdProductNumMap(Set<Long> categoryIdList);

	/**
	 * 根据 id 查询后台分类
	 *
	 * @param categoryId 后台分类 id
	 * @return 后台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/13 14:37
	 */
	BackCategoryEntity queryById(Long categoryId);

	/**
	 * 查询后台分类集合,组装成树形结构
	 *
	 * @param categoryQueryRequest 查询条件
	 * @return 后台分类的集合, 树形结构
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<BackCategoryVO> queryByParamToTree(CategoryQueryRequest categoryQueryRequest);
}
