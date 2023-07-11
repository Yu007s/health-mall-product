package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.facade.activty.ActivityPackageManageFacade;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.activty.ActivityPackageRemoteApi;
import com.drstrong.health.product.remote.api.product.SkuManageRemoteApi;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * huangpeng
 * 活动套餐入口
 * 2023/7/5 17:44
 */
@RestController
@RequestMapping("/inner/activityPackage/manage")
public class ActivityPackageManageRemoteController implements ActivityPackageRemoteApi {

    @Autowired
    private ActivityPackageManageFacade activityPackageManageFacade;

    /**
     * 关键字列表分页查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public ResultVO<PageVO<ActivityPackageInfoVO>> pageQuerySkuManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(activityPackageManageFacade.queryActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 关键词搜索数据导出
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public ResultVO<List<ActivityPackageInfoVO>> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(activityPackageManageFacade.listActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 套餐新增/编辑
     *
     * @param saveOrUpdateActivityPackageRequest
     * @return
     */
    @Override
    public ResultVO<Void> saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest) {
        activityPackageManageFacade.addLocksaveOrUpdateActivityPackage(saveOrUpdateActivityPackageRequest, saveOrUpdateActivityPackageRequest.getActivityPackageSkuList().get(0).getSkuCode());
        return ResultVO.success();
    }

    /**
     * 查询套餐详情(根据套餐编码)
     *
     * @param activityPackageCode
     * @return
     */
    @Override
    public ResultVO<ActivityPackageDetailDTO> queryDetailByCode(String activityPackageCode) {
        return ResultVO.success(activityPackageManageFacade.queryDetailByCode(activityPackageCode));
    }

    /**
     * 上下架
     *
     * @param updateSkuStateRequest
     * @return
     */
    @Override
    public ResultVO<Void> updateActivityPackageStatus(UpdateSkuStateRequest updateSkuStateRequest) {
        updateSkuStateRequest.setScheduledStatus(ScheduledStatusEnum.CANCEL.getCode());
        activityPackageManageFacade.updateActivityPackageStatus(updateSkuStateRequest);
        return ResultVO.success();
    }

    /**
     * 预约上下架
     *
     * @param scheduledSkuUpDownRequest
     * @return
     */
    @Override
    public ResultVO<Void> scheduledActivityPackageUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest) {
        activityPackageManageFacade.scheduledActivityPackageUpDown(scheduledSkuUpDownRequest);
        return ResultVO.success();
    }

    /**
     * 医生端套餐列表查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public ResultVO<PageVO<ActivityPackageInfoVO>> queryActivityPackageList(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(activityPackageManageFacade.queryActivityPackageList(activityPackageManageQueryRequest));
    }

}
