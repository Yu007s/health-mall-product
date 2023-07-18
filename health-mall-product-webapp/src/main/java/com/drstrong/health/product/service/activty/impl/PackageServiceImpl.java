package com.drstrong.health.product.service.activty.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.activty.ActivityPackageInfoMapper;
import com.drstrong.health.product.dao.sku.StoreSkuInfoMapper;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.activty.PackageService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/7 12:01
 */
@Slf4j
@Service
public class PackageServiceImpl extends ServiceImpl<ActivityPackageInfoMapper, ActivityPackageInfoEntity> implements PackageService {

    /**
     * 套餐编码查询套餐信息
     *
     * @param activityPackageCode
     * @param activityStatus
     * @return
     */
    @Override
    public ActivityPackageInfoEntity findPackageByCode(String activityPackageCode, Integer activityStatus) {
        if (StrUtil.isBlank(activityPackageCode)) {
            return null;
        }
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ActivityPackageInfoEntity::getActivityPackageCode, activityPackageCode)
                .eq(Objects.nonNull(activityStatus), ActivityPackageInfoEntity::getActivityStatus, activityStatus);
        ActivityPackageInfoEntity activityPackageInfoEntity = baseMapper.selectOne(queryWrapper);
        if (Objects.isNull(activityPackageInfoEntity)) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_IS_NULL);
        }
        return activityPackageInfoEntity;
    }

    /**
     * 套餐编码查询套餐信息
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public List<ActivityPackageInfoEntity> findPackageByCodes(List<String> activityPackageCode) {
        if (CollectionUtil.isEmpty(activityPackageCode)) {
            return null;
        }
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ActivityPackageInfoEntity::getActivityPackageCode, activityPackageCode);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 条件分页列表查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public Page<ActivityPackageInfoEntity> pageQueryByParam(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        Page<ActivityPackageInfoEntity> entityPage = new Page<>(activityPackageManageQueryRequest.getPageNo(), activityPackageManageQueryRequest.getPageSize());
        return baseMapper.pageQueryByParam(entityPage, activityPackageManageQueryRequest);
    }

    @Override
    public List<ActivityPackageInfoEntity> listQueryByParam(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return baseMapper.listQueryByParam(activityPackageManageQueryRequest);
    }

    /**
     * 根据套餐编码列表批量查询套餐
     *
     * @param activityPackageCodeList
     * @return
     */
    @Override
    public List<ActivityPackageInfoEntity> queryByActivityPackageCode(Set<String> activityPackageCodeList) {
        if (CollectionUtil.isEmpty(activityPackageCodeList)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ActivityPackageInfoEntity::getActivityPackageCode, activityPackageCodeList);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 状态修改
     *
     * @param activityPackageCodeList
     * @param skuState
     * @param operatorId
     */
    @Override
    public void batchUpdateActivityStatusByCodes(Set<String> activityPackageCodeList, Integer skuState, Long operatorId) {
        int size = baseMapper.batchUpdateActivityStatusByCodes(activityPackageCodeList, skuState, operatorId);
        if (size > 0) {
            log.info("套餐Activity信息表,已将activityPackageCodeList列表:{} 的状态更新为:{},操作人是:{}", activityPackageCodeList, skuState, operatorId);
        }
    }

    /**
     * 根据套餐ID获取详情
     *
     * @param activityPackageId
     * @return
     */
    @Override
    public ActivityPackageInfoEntity findPackageById(Long activityPackageId) {
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ActivityPackageInfoEntity::getId, activityPackageId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据storeId和productType获取套餐列表
     *
     * @param storeId
     * @param productType
     * @return
     */
    @Override
    public List<ActivityPackageInfoEntity> queryAllByProductType(Long storeId, Integer productType) {
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ActivityPackageInfoEntity::getStoreId, storeId)
                .eq(ActivityPackageInfoEntity::getProductType, productType);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 医生端的列表套餐搜索
     *
     * @param packageBussinessQueryListRequest
     * @return
     */
    @Override
    public Page<ActivityPackageInfoEntity> pageQueryList(PackageBussinessQueryListRequest packageBussinessQueryListRequest) {
        Page<ActivityPackageInfoEntity> entityPage = new Page<>(packageBussinessQueryListRequest.getPageNo(), packageBussinessQueryListRequest.getPageSize());
        return baseMapper.pageQueryList(entityPage, packageBussinessQueryListRequest);
    }

}
