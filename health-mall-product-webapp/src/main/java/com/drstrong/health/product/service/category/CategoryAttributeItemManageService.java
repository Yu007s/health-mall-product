package com.drstrong.health.product.service.category;

import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;

/**
 * 分类属性管理 service
 *
 * @author liuqiuyi
 * @date 2021/12/27 20:00
 */
public interface CategoryAttributeItemManageService {
	/**
	 * 保存分类属性项
	 * <p> 参照之前的代码,未改动 </>
	 *
	 * @param vo 分类属性项数据
	 */
	CategoryAttributeItemEntity saveItem(CategoryAttributeItemEntity vo) throws BusinessException;

	/**
	 * 删除分类属性项
	 *
	 * @param attributeItemId 分类属性项ID
	 */
	CategoryAttributeItemEntity deleteItem(Long attributeItemId) throws BusinessException;
}
