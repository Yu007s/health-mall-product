package com.drstrong.health.product.controller.activty;

import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.facade.activty.PackageBussinessFacade;
import com.drstrong.health.product.facade.activty.PackageManageFacade;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.PackageBussinessQueryListRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageBussinessListVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.activty.PackageBussinessRemoteApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * huangpeng
 * 客户端套餐接口
 * 2023/7/11 17:55
 */
@RestController
@RequestMapping("/inner/package/bussiness")
public class PackageBusinessRemoteController implements PackageBussinessRemoteApi {

    @Autowired
    private PackageBussinessFacade packageBussinessFacade;

    /**
     * 查询套餐详情(根据套餐编码)
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public ResultVO<ActivityPackageDetailDTO> queryDetailByCode(String activityPackageCode) {
        return ResultVO.success(packageBussinessFacade.queryDetailByCode(activityPackageCode));
    }

    /**
     * 医生端套餐条件列表查询
     *
     * @param packageBussinessQueryListRequest
     * @return
     */
    @Override
    public ResultVO<PageVO<PackageBussinessListVO>> queryActivityPackageList(PackageBussinessQueryListRequest packageBussinessQueryListRequest) {
        return ResultVO.success(packageBussinessFacade.queryActivityPackageList(packageBussinessQueryListRequest));
    }
}
