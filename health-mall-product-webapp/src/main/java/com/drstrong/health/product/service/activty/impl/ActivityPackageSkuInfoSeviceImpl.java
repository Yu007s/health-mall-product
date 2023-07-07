package com.drstrong.health.product.service.activty.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.drstrong.health.product.dao.activty.ActivityPackageInfoMapper;
import com.drstrong.health.product.dao.activty.ActivityPackageSkuInfoMapper;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.enums.DelFlagEnum;
import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.service.activty.ActivityPackageSkuInfoSevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * huangpeng
 * 2023/7/7 13:57
 */
@Slf4j
@Service
public class ActivityPackageSkuInfoSeviceImpl extends ServiceImpl<ActivityPackageSkuInfoMapper, ActivityPackageSkuInfoEntity> implements ActivityPackageSkuInfoSevice {

    /**
     * 目前套餐限制的sku药品种类
     */
    private final static Integer LIMITED_NUMBER_OF_PACHAGES_SKUS = 1;

    /**
     * 根据套餐编码获取套餐sku列表信息
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public List<ActivityPackageSkuInfoEntity> findPackageByCode(String activityPackageCode) {
        if (StrUtil.isBlank(activityPackageCode)) {
            return null;
        }
        LambdaQueryWrapper<ActivityPackageSkuInfoEntity> queryWrapper = new LambdaQueryWrapper<ActivityPackageSkuInfoEntity>()
                .eq(ActivityPackageSkuInfoEntity::getDelFlag, DelFlagEnum.UN_DELETED.getCode())
                .eq(ActivityPackageSkuInfoEntity::getActivityPackageCode, activityPackageCode);
        List<ActivityPackageSkuInfoEntity> activityPackageSkuInfoEntities = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(activityPackageSkuInfoEntities)) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_IS_NULL);
        }
        if (activityPackageSkuInfoEntities.size() > LIMITED_NUMBER_OF_PACHAGES_SKUS) {
            throw new BusinessException(ErrorEnums.ACTIVTY_PACKAGE_SKU_MORE_THAN_ONE);
        }
        return activityPackageSkuInfoEntities;
    }
}
