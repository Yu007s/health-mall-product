package com.drstrong.health.product.facade.activty;

import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageBussinessListVO;

import java.util.List;

/**
 * huangpeng
 * 2023/7/5 17:49
 */
public interface PackageBussinessFacade {

    ActivityPackageDetailDTO queryDetailByCode(String activityPackageCode);

    PageVO<PackageBussinessListVO> queryActivityPackageList(PackageBussinessQueryListRequest packageBussinessQueryListRequest);

}
