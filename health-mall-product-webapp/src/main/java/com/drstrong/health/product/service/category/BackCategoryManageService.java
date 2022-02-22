package com.drstrong.health.product.service.category;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.response.result.BusinessException;

/**
 * 后台分类管理端相关接口
 *
 * @author liuqiuyi
 * @date 2021/12/27 17:46
 */
public interface BackCategoryManageService {
	/**
	 * 保存或更新分类
	 * @param entity 分类数据: id为空时新增，不为空时更新
	 */
	BackCategoryEntity saveEntity(BackCategoryEntity entity) throws BusinessException;

	/**
	 * 删除分类
	 * @param categoryId 分类ID
	 */
	BackCategoryEntity deleteEntity(Long categoryId) throws BusinessException;

	/**
	 * 更新分类状态
	 * @param categoryId 分类ID
	 * @param status 状态值：0-禁用，1-启用
	 */
	void updateStatus(Long categoryId, Integer status);
}
