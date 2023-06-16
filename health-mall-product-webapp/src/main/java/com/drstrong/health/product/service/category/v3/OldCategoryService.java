package com.drstrong.health.product.service.category.v3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.response.result.BusinessException;

import java.util.List;

/**
 * 老的 cms 中的代码,直接迁移过来的
 *
 * @author liuqiuyi
 * @date 2023/6/16 16:16
 */
public interface OldCategoryService extends IService<CategoryEntity> {

	/**
	 * 获取分类树
	 *
	 * @param level  分类级别：如果为空，返回所有分类树
	 * @param status 分类状态： 0-禁用，1-启用
	 */
	List<CategoryEntity> getTree(Integer level, Integer status);

	/**
	 * 保存或更新分类
	 *
	 * @param entity 分类数据: id为空时新增，不为空时更新
	 */
	CategoryEntity saveEntity(CategoryEntity entity) throws BusinessException;

	/**
	 * 删除分类
	 *
	 * @param CategoryEntityId 分类ID
	 */
	CategoryEntity deleteEntity(Long CategoryEntityId) throws BusinessException;
}
