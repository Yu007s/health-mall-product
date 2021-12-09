package com.drstrong.health.product.service;

import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;

import java.util.List;

/**
 * 前台分类 service 接口
 *
 * @author liuqiuyi
 * @date 2021/12/7 20:02
 */
public interface FrontCategoryService {
	/**
	 * 查询所有的前台分类
	 *
	 * @param categoryQueryRequest 查询的参数信息
	 * @return 前台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<FrontCategoryResponse> queryByParam(CategoryQueryRequest categoryQueryRequest);

}
