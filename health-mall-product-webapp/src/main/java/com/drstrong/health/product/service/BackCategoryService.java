package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;

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
	 * 查询所有的后台分类
	 *
	 * @param categoryIdList 分类 id 集合
	 * @return 后台分类的集合
	 * @author liuqiuyi
	 * @date 2021/12/7 20:37
	 */
	List<BackCategoryEntity> queryByIdList(Set<Long> categoryIdList);
}
