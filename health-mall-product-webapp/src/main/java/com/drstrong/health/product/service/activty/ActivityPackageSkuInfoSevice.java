package com.drstrong.health.product.service.activty;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;

import java.util.List;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/7 13:56
 */
public interface ActivityPackageSkuInfoSevice extends IService<ActivityPackageSkuInfoEntity> {

    List<ActivityPackageSkuInfoEntity> findPackageByCode(String activityPackageCode);

    List<ActivityPackageSkuInfoEntity>  queryBySkuCodeAndAmount(String skuCode, Integer amount);

    List<ActivityPackageSkuInfoEntity> queryBySkuCodes(List<String> skuCodes);

    List<ActivityPackageSkuInfoEntity> queryUpPackageSku();
}
