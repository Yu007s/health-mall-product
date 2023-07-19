package com.drstrong.health.product.service.activty.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.activty.ActivityPackageInfoMapper;
import com.drstrong.health.product.enums.ActivityStatusEnum;
import com.drstrong.health.product.model.dto.product.PackageInfoVO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import com.drstrong.health.product.service.activty.PackageService;
import com.drstrong.health.product.util.BigDecimalUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

/**
 * huangpeng
 * 2023/7/7 12:01
 */
@Slf4j
@Service
public class PackageServiceImpl extends ServiceImpl<ActivityPackageInfoMapper, ActivityPackageInfoEntity> implements PackageService {

    @Autowired
    private ActivityPackageSkuInfoSevice activityPackageSkuInfoSevice;

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

    /**
     * 条件列表查询(不分页)
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
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
     * 套餐定时上下架检索套餐
     *
     * @return
     */
    @Override
    public List<ActivityPackageInfoEntity> findScheduledPackage() {
        List<Integer> activityStatusList = Lists.newArrayList(ActivityStatusEnum.TO_BE_STARTED.getCode(), ActivityStatusEnum.UNDER_WAY.getCode());
        LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .in(ActivityPackageInfoEntity::getActivityStatus, activityStatusList);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 批量更新套餐活动状态
     *
     * @param packageCodes
     * @param code
     */
    @Override
    public void updateActivityStatus(Set<String> packageCodes, Integer code) {
        if (CollectionUtil.isEmpty(packageCodes) || Objects.isNull(code)) {
            return;
        }
        int size = baseMapper.batchUpdateActivityStatusByCodes(packageCodes, code);
    }

    /**
     * 查询套餐商品和关联的套餐信息
     *
     * @return
     */
    @Override
    public Map<String, List<PackageInfoVO>> getUpPackageInfo() {
        List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntities = activityPackageSkuInfoSevice.queryUpPackageSku();
        if (CollectionUtil.isEmpty(activityPackageSkuInfoEntities)) {
            return null;
        }
        return buildPackageInfoResult(activityPackageSkuInfoEntities);
    }

    /**
     * 查询套餐商品和关联的套餐信息
     *
     * @return
     */
    @Override
    public Map<String, List<PackageInfoVO>> getUpPackageInfo(List<String> skuCodeList) {
        List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntities = activityPackageSkuInfoSevice.queryBySkuCodes(skuCodeList);
        if (CollectionUtil.isEmpty(activityPackageSkuInfoEntities)) {
            return null;
        }
        return buildPackageInfoResult(activityPackageSkuInfoEntities);
    }

    private Map<String, List<PackageInfoVO>> buildPackageInfoResult(List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntities) {
        Map<String, List<PackageInfoVO>> result = new HashMap<>();
        Map<String, ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntityMap = activityPackageSkuInfoEntities.stream().collect(toMap(ActivityPackageSkuInfoEntity::getActivityPackageCode, dto -> dto, (v1, v2) -> v1));
        Map<String, List<ActivityPackageSkuInfoEntity>> activityPackageSkuInfoEntitiesMap = activityPackageSkuInfoEntities.stream().collect(Collectors.groupingBy(ActivityPackageSkuInfoEntity::getSkuCode));
        for (String key : activityPackageSkuInfoEntitiesMap.keySet()) {
            List<ActivityPackageSkuInfoEntity> skuInfoEntities = activityPackageSkuInfoEntitiesMap.get(key);
            List<String> activityPackageCodeList = skuInfoEntities.stream().map(ActivityPackageSkuInfoEntity::getActivityPackageCode).collect(Collectors.toList());
            LambdaQueryWrapper<ActivityPackageInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageInfoEntity>()
                    .eq(ActivityPackageInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                    .eq(ActivityPackageInfoEntity::getActivityStatus, ActivityStatusEnum.UNDER_WAY.getCode())
                    .in(ActivityPackageInfoEntity::getActivityPackageCode, activityPackageCodeList);
            List<ActivityPackageInfoEntity> activityPackageInfoEntityList = baseMapper.selectList(queryWrapper);
            if (CollectionUtil.isEmpty(activityPackageInfoEntityList)) {
                continue;
            }
            List<PackageInfoVO> packageInfoVOList = new ArrayList<>();
            for (ActivityPackageInfoEntity activityPackageInfoEntity : activityPackageInfoEntityList) {
                PackageInfoVO packageInfoVO = PackageInfoVO.builder()
                        .activityPackageName(activityPackageInfoEntity.getActivityPackageName())
                        .activityPackageCode(activityPackageInfoEntity.getActivityPackageCode())
                        .price(BigDecimalUtil.F2Y(activityPackageInfoEntity.getPrice()))
                        .activityPackageRemark(activityPackageInfoEntity.getActivityPackageRemark())
                        .amount(activityPackageSkuInfoEntityMap.get(activityPackageInfoEntity.getActivityPackageCode()).getAmount())
                        .skuName(activityPackageSkuInfoEntityMap.get(activityPackageInfoEntity.getActivityPackageCode()).getSkuName())
                        .skuPrice(BigDecimalUtil.F2Y(activityPackageSkuInfoEntityMap.get(activityPackageInfoEntity.getActivityPackageCode()).getPreferentialPrice()))
                        .skuCode(activityPackageSkuInfoEntityMap.get(activityPackageInfoEntity.getActivityPackageCode()).getSkuCode())
                        .build();
                packageInfoVOList.add(packageInfoVO);
            }
            result.put(key, packageInfoVOList);
        }
        return result;
    }

}
