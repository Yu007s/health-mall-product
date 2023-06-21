package com.drstrong.health.product.service.category.v3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
import com.drstrong.health.product.model.request.category.v3.SaveCategoryRequest;
import com.drstrong.health.product.model.request.category.v3.SearchCategoryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
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
	 * @param saveCategoryRequest 分类数据: id为空时新增，不为空时更新
	 */
	CategoryEntity saveEntity(SaveCategoryRequest saveCategoryRequest, Integer productType) throws BusinessException;

	/**
	 * 删除分类
	 *
	 * @param categoryEntityId 分类ID
	 */
	CategoryEntity deleteEntity(Long categoryEntityId, String changedName) throws BusinessException;

	/**
	 * 查询健康用品的列表信息，参照之前的老代码
	 *
	 * @author liuqiuyi
	 * @date 2023/6/21 10:27
	 */
    PageVO<CategoryVO> pageSearch(SearchCategoryRequest searchCategoryRequest);

    /**
     * 删除分类，参照之前的老代码
     *
     * @author liuqiuyi
     * @date 2023/6/21 10:51
     */
	void updateCategoryStatus(Long categoryId, Integer status, String changedName);
}
