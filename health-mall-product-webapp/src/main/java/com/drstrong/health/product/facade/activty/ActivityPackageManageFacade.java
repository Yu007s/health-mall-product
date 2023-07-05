package com.drstrong.health.product.facade.activty;

import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;

import java.util.List;

/**
 * huangpeng
 * 2023/7/5 17:49
 */
public interface ActivityPackageManageFacade {

    PageVO<ActivityPackageInfoVO> queryActivityPackageManageInfo(ActivityPackageManageQueryRequest productManageQueryRequest);

    List<ActivityPackageInfoVO> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    void addLocksaveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest, String skuCode);
}
