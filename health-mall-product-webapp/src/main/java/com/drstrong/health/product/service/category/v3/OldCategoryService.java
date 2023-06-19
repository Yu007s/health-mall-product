package com.drstrong.health.product.service.category.v3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.response.result.BusinessException;

/**
 * 老的 cms 中的代码,直接迁移过来的
 *
 * @author liuqiuyi
 * @date 2023/6/16 16:16
 */
public interface OldCategoryService extends IService<CategoryEntity> {
	/**
	 * 保存或更新分类
	 *
	 * @param entity 分类数据: id为空时新增，不为空时更新
	 */
	CategoryEntity saveEntity(CategoryEntity entity, Integer productType) throws BusinessException;

	/**
	 * 删除分类
	 *
	 * @param categoryEntityId 分类ID
	 */
	CategoryEntity deleteEntity(Long categoryEntityId, String changedName) throws BusinessException;
}