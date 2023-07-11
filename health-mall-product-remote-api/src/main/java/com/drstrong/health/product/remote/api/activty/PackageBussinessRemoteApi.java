package com.drstrong.health.product.remote.api.activty;

import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * huangpeng
 * 2023/7/8 14:47
 */
@Api("客户端的套餐远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/package/bussiness")
public interface PackageBussinessRemoteApi {

    @ApiOperation("根据Code查询套餐详情")
    @GetMapping("/query/by-code")
    ResultVO<ActivityPackageDetailDTO> queryDetailByCode(String activityPackageCode);

    @ApiOperation("医生端套餐列表查询")
    @PostMapping("/page/queryList")
    ResultVO<PageVO<ActivityPackageInfoVO>> queryActivityPackageList(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);
}
