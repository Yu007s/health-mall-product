package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;

import java.util.List;
import java.util.Set;

/**
 * 后台分类 service 接口
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:02
 */
public interface BackCategoryService {
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
	 * 查询后台分类集合
	 *
	 * @param categoryQueryRequest 查询条件
	 * @return 后台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<BackCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest);
}
