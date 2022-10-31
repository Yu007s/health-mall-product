package com.drstrong.health.product.controller;

import com.drstrong.health.product.controller.datasync.IChineseMedicineAliasService;
import com.drstrong.health.product.controller.datasync.IChineseMedicineConflictService;
import com.drstrong.health.product.controller.datasync.IChineseMedicineService;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.DataSyncFacade;
import com.drstrong.health.product.service.category.BackCategoryManageService;
import com.drstrong.health.product.service.category.CategoryAttributeItemManageService;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据同步的接口
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:17
 */
@RestController
@RequestMapping("/inner/product/data/sync")
@Slf4j
@Deprecated
public class DataSyncController implements DataSyncFacade {

    @Resource
    BackCategoryManageService backCategoryManageService;

    @Resource
    CategoryAttributeItemManageService categoryAttributeItemManageService;

    @Resource
    IChineseMedicineService oldChineseMedicineService;

    @Resource
    ChineseMedicineService newChineseMedicineService;

    @Resource
    IChineseMedicineAliasService chineseMedicineAliasService;

    @Resource
    IChineseMedicineConflictService oldChineseMedicineConflictService;
    @Resource
    ChineseMedicineConflictService newChineseMedicineConflictService;

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
