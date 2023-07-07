package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.facade.activty.ActivityPackageManageFacade;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * huangpeng
 * 活动套餐管理(cms)
 * 2023/7/5 17:44
 */
@RestController
@RequestMapping("/inner/activityPackage/manage")
public class ActivityPackageManageRemoteController {

    @Autowired
    private ActivityPackageManageFacade activityPackageManageFacade;

    /**
     * 关键字列表分页查询
     * @param activityPackageManageQueryRequest
     * @return
     */
    public ResultVO<PageVO<ActivityPackageInfoVO>> pageQuerySkuManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(activityPackageManageFacade.queryActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 关键词搜索数据导出
     * @param activityPackageManageQueryRequest
     * @return
     */
    public ResultVO<List<ActivityPackageInfoVO>> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(activityPackageManageFacade.listActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 套餐新增/编辑
     * @param saveOrUpdateActivityPackageRequest
     * @return
     */
    public ResultVO<Void> saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest) {
        activityPackageManageFacade.addLocksaveOrUpdateActivityPackage(saveOrUpdateActivityPackageRequest, saveOrUpdateActivityPackageRequest.getActivityPackageSkuList().get(0).getSkuCode());
        return ResultVO.success();
    }

    /**
     * 查询套餐详情
     * @param activityPackageCode
     * @return
     */
    public ResultVO<ActivityPackageDetailDTO> queryDetailByCode(String activityPackageCode) {
        return ResultVO.success(activityPackageManageFacade.queryDetailByCode(activityPackageCode));
    }

}
