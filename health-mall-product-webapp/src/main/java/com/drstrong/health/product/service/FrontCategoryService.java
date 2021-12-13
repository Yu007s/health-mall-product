package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;

import java.util.List;
import java.util.Set;

/**
 * 前台分类 service 接口
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:02
 */
public interface FrontCategoryService {
	/**
	 * 查询所有的前台分类,并组装树形结构
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<FrontCategoryResponse> queryTreeByParam(CategoryQueryRequest categoryQueryRequest);

	/**
	 * 根据分类 id 集合查询分类信息
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 前台分类集合信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	List<FrontCategoryEntity> queryByIdList(Set<Long> categoryIdList);

	/**
	 * 根据分类 id 查询分类信息
	 *
	 * @param categoryId 分类 id
	 * @return 前台分类信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:41
	 */
	FrontCategoryEntity queryById(Long categoryId);

	/**
	 * 添加前台分类
	 *
	 * @param categoryRequest 入参信息
	 * @author liuqiuyi
	 * @date 2021/12/10 17:19
	 */
	void add(AddOrUpdateFrontCategoryRequest categoryRequest);

	/**
	 * 更新前台分类信息
	 *
	 * @param updateFrontCategoryRequest 更新前台分类的参数
	 * @author liuqiuyi
	 * @date 2021/12/13 10:14
	 */
	void update(AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest);

	/**
	 * 更新前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	void updateStateFront(Long categoryId, Long userId);

	/**
	 * 逻辑删除前台分类状态
	 *
	 * @param categoryId 前台分类 id
	 * @param userId     用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:05
	 */
	void deleteFrontCategoryById(Long categoryId, Long userId);
}
