package com.drstrong.health.product.facade.activty;

import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.sku.SkuBusinessListDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.request.sku.QuerySkuBusinessListRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageManageListVO;

import java.util.List;

/**
 * huangpeng
 * 2023/7/5 17:49
 */
public interface PackageManageFacade {

    PageVO<PackageManageListVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest productManageQueryRequest);

    List<PackageManageListVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    void addLocksaveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, String skuCode);

    void saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest);

    ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode);

    PageVO<SkuBusinessListDTO> getPackageSkuBusinessList(QuerySkuBusinessListRequest querySkuBusinessListRequest);

    void doScheduledUpDown();

}
