package com.drstrong.health.product.remote.api.activty;

import com.drstrong.health.product.enums.ScheduledStatusEnum;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.product.StoreSkuDetailDTO;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.request.product.v3.SaveOrUpdateStoreSkuRequest;
import com.drstrong.health.product.model.request.product.v3.ScheduledSkuUpDownRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ActivityPackageInfoVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
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
@Api("健康商城-套餐服务-套餐管理页面的远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/package/manage")
public interface ActivityPackageRemoteApi {

    @ApiOperation("套餐管理页面查询")
    @PostMapping("/page/query")
    ResultVO<PageVO<ActivityPackageInfoVO>> pageQuerySkuManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    @ApiOperation("sku管理页面查询所有数据,导出需要")
    @PostMapping("/query/all")
    ResultVO<List<ActivityPackageInfoVO>> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest);

    @ApiOperation("保存或更新店铺的套餐信息")
    @PostMapping("/store/save-or-update/package")
    ResultVO<Void> saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest);

    @ApiOperation("根据Code查询套餐详情")
    @GetMapping("/query/by-code")
    ResultVO<ActivityPackageDetailDTO> queryDetailByCode(String activityPackageCode);

    @ApiOperation("批量上下架")
    @PostMapping("/update/sku-status")
    ResultVO<Void> updateActivityPackageStatus(UpdateSkuStateRequest updateSkuStateRequest);

    @ApiOperation("预约上下架")
    @PostMapping("/scheduled/up-or-down")
    ResultVO<Void> scheduledActivityPackageUpDown(ScheduledSkuUpDownRequest scheduledSkuUpDownRequest);
}
