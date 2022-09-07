package com.drstrong.health.product.controller;

import com.drstrong.health.product.controller.datasync.IChineseMedicineAliasService;
import com.drstrong.health.product.controller.datasync.IChineseMedicineConflictService;
import com.drstrong.health.product.controller.datasync.IChineseMedicineService;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineAlias;
import com.drstrong.health.product.controller.datasync.model.ChineseMedicineConflict;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.DataSyncFacade;
import com.drstrong.health.product.service.category.BackCategoryManageService;
import com.drstrong.health.product.service.category.CategoryAttributeItemManageService;
import com.drstrong.health.product.service.chinese.ChineseMedicineConflictService;
import com.drstrong.health.product.service.chinese.ChineseMedicineService;
import com.drstrong.health.product.service.other.DataSyncServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

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
    DataSyncServiceImpl dataSyncService;

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

    /**
     * 中药材库数据迁移  注意迁移顺序  要先迁移中药材表  再往下进行
     *
     * @param id 中药材id
     * @return 返回最后的id
     */
    @GetMapping("/medicine")
    public String transDbMedicine(Long id) {
        List<OldChineseMedicine> medicines;
        do {
            Long aLong = id == null ? 1L : id;
            medicines = oldChineseMedicineService.getMedicines(aLong);
            //迁移药材表
			newChineseMedicineService.insertBatch(medicines);
            if (medicines.size() == 0) {
                break;
            }
            id = (long) medicines.get(medicines.size() - 1).getId() + 1;
        } while (medicines.size() != 0);
        return "success last id is " + id;
    }

    @GetMapping("/alias")
    public String transAlias(Long id) {
        //迁移药材别名表
        HashMap<Long, List<ChineseMedicineAlias>> alias;
        do {
            id = id == null ? 1L : id;
            alias = chineseMedicineAliasService.getAlias(id);
			newChineseMedicineService.updateAlias(alias);
            id = alias.size() == 0 ? id : alias.keySet().stream().max((a, b) -> (int) (a - b)).orElse(id) + 1;
        } while (alias.size() != 0);
        return "success last medicine_id is " + id;
    }

    @GetMapping("/conflict")
    public String transConflict(Long id) {
        HashMap<Long, List<ChineseMedicineConflict>> conflicts;
        do {
            id = id == null ? 1L : id;
            conflicts = oldChineseMedicineConflictService.getConflicts(id);
            Long aLong = conflicts.keySet().stream().max((a, b) -> (int) (a - b)).orElse(id);
			newChineseMedicineConflictService.updateFromOld(conflicts);
            id = aLong + 1;
        }
        while (conflicts.size() != 0);
        return "success last id is " + id;
    }

}
