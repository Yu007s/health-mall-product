package com.drstrong.health.product.service.activty;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;

/**
 * huangpeng
 * 2023/7/7 11:50
 */
public interface ActivityPackageInfoService extends IService<ActivityPackageInfoEntity> {

    ActivityPackageInfoEntity findPackageByCode(String activityPackageCode, Integer activityStatus);
}
