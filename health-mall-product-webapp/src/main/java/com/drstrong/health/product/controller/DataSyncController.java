package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.DataSyncFacade;
import com.drstrong.health.product.service.category.BackCategoryManageService;
import com.drstrong.health.product.service.category.CategoryAttributeItemManageService;
import com.drstrong.health.product.service.other.DataSyncServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据同步的接口
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:17
 */
@RestController
@RequestMapping("/data/sync")
@Slf4j
public class DataSyncController implements DataSyncFacade {
	@Resource
	DataSyncServiceImpl dataSyncService;

	@Resource
	BackCategoryManageService backCategoryManageService;

	@Resource
	CategoryAttributeItemManageService categoryAttributeItemManageService;

	/**
	 * 同步 p_category 表的数据
	 *
	 * @param categoryEntityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:10
	 */
	@Override
	public ResultVO<Object> categorySync(List<BackCategoryEntity> categoryEntityList) {
		dataSyncService.categorySync(categoryEntityList);
		return ResultVO.success();
	}

	/**
	 * 同步 p_category_attribute 表的数据
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:15
	 */
	@Override
	public ResultVO<Object> categoryAttributeSync(List<CategoryAttributeEntity> entityList) {
		dataSyncService.categoryAttributeSync(entityList);
		return ResultVO.success();
	}

	/**
	 * 同步 p_category_attribute_item 表
	 *
	 * @param entityList
	 * @author liuqiuyi
	 * @date 2021/12/27 11:16
	 */
	@Override
	public ResultVO<Object> categoryAttributeItem(List<CategoryAttributeItemEntity> entityList) {
		dataSyncService.categoryAttributeItem(entityList);
		return ResultVO.success();
	}

	/**
	 * p_category 保存或更新分类
	 *
	 * @param entity
	 * @author liuqiuyi
	 * @date 2021/12/28 09:42
	 */
	@Override
	public ResultVO<BackCategoryEntity> categorySaveEntity(BackCategoryEntity entity) throws BusinessException {
		BackCategoryEntity backCategoryEntity = backCategoryManageService.saveEntity(entity);
		return ResultVO.success(backCategoryEntity);
	}

	/**
	 * p_category 删除分类
	 *
	 * @param categoryId 分类ID
	 */
	@Override
	public ResultVO<BackCategoryEntity> categoryDeleteEntity(Long categoryId) throws BusinessException {
		BackCategoryEntity backCategoryEntity = backCategoryManageService.deleteEntity(categoryId);
		return ResultVO.success(backCategoryEntity);
	}

	/**
	 * 更新分类状态
	 *
	 * @param categoryId 分类ID
	 * @param status     状态值：0-禁用，1-启用
	 */
	@Override
	public ResultVO<Object> categoryUpdateStatus(Long categoryId, Integer status) {
		backCategoryManageService.updateStatus(categoryId, status);
		return ResultVO.success();
	}

	/**
	 * p_category_attribute_item 保存分类属性项
	 *
	 * @param vo
	 * @author liuqiuyi
	 * @date 2021/12/28 09:50
	 */
	@Override
	public ResultVO<CategoryAttributeItemEntity> categoryAttributeItemSaveItem(CategoryAttributeItemEntity vo) throws BusinessException {
		CategoryAttributeItemEntity itemEntity = categoryAttributeItemManageService.saveItem(vo);
		return ResultVO.success(itemEntity);
	}

	/**
	 * p_category_attribute_item 删除分类属性项
	 *
	 * @param attributeItemId
	 * @author liuqiuyi
	 * @date 2021/12/28 09:50
	 */
	@Override
	public ResultVO<CategoryAttributeItemEntity> categoryAttributeItemDeleteItem(Long attributeItemId) throws BusinessException {
		CategoryAttributeItemEntity itemEntity = categoryAttributeItemManageService.deleteItem(attributeItemId);
		return ResultVO.success(itemEntity);
	}
}
