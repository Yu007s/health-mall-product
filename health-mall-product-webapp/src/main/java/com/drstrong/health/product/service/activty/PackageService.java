package com.drstrong.health.product.service.activty;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.dto.product.PackageInfoVO;
import com.drstrong.health.product.model.entity.activty.ActivityPackageInfoEntity;
import com.drstrong.health.product.model.entity.sku.StoreSkuInfoEntity;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * huangpeng
 * 2023/7/7 11:50
 */
public interface PackageService extends IService<ActivityPackageInfoEntity> {

    ActivityPackageInfoEntity findPackageByCode(String activityPackageCode, Integer activityStatus);

    Page<ActivityPackageInfoEntity> pageQueryByParam(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    List<ActivityPackageInfoEntity> listQueryByParam(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    List<ActivityPackageInfoEntity> queryByActivityPackageCode(Set<String> skuCodeList);

    List<ActivityPackageInfoEntity> queryAllByProductType(Long storeId, Integer productType);

    List<ActivityPackageInfoEntity> findScheduledPackage();

    void updateActivityStatus(Set<String> packageCodes, Integer code);

    Map<String, List<PackageInfoVO>> getUpPackageInfo();

    Map<String, List<PackageInfoVO>> getUpPackageInfo(List<String> skuCodeList);

}
