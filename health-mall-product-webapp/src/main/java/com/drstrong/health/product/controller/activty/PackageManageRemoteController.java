package com.drstrong.health.product.controller.activty;

import com.drstrong.health.product.facade.activty.PackageManageFacade;
import com.drstrong.health.product.model.dto.product.ActivityPackageDetailDTO;
import com.drstrong.health.product.model.dto.sku.SkuBusinessListDTO;
import com.drstrong.health.product.model.request.product.ActivityPackageManageQueryRequest;
import com.drstrong.health.product.model.request.product.SaveOrUpdateActivityPackageRequest;
import com.drstrong.health.product.model.request.sku.QuerySkuBusinessListRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.PackageManageListVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.activty.PackageManageRemoteApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * huangpeng
 * 活动套餐入口
 * 2023/7/5 17:44
 */
@RestController
@RequestMapping("/inner/package/manage")
public class PackageManageRemoteController implements PackageManageRemoteApi {

    @Autowired
    private PackageManageFacade packageManageFacade;

    /**
     * 关键字列表分页查询
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public ResultVO<PageVO<PackageManageListVO>> pageQuerySkuManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(packageManageFacade.queryActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 关键词搜索数据导出
     *
     * @param activityPackageManageQueryRequest
     * @return
     */
    @Override
    public ResultVO<List<PackageManageListVO>> listActivityPackageManageInfo(ActivityPackageManageQueryRequest activityPackageManageQueryRequest) {
        return ResultVO.success(packageManageFacade.listActivityPackageManageInfo(activityPackageManageQueryRequest));
    }

    /**
     * 套餐新增/编辑
     *
     * @param saveOrUpdateActivityPackageRequest
     * @return
     */
    @Override
    public ResultVO<Void> saveOrUpdateActivityPackage(SaveOrUpdateActivityPackageRequest saveOrUpdateActivityPackageRequest) {
        packageManageFacade.addLocksaveOrUpdateActivityPackage(saveOrUpdateActivityPackageRequest, saveOrUpdateActivityPackageRequest.getActivityPackageSkuList().get(0).getSkuCode());
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
        return ResultVO.success(packageManageFacade.queryDetailByCode(activityPackageCode));
    }

    /**
     * 添加套餐时的sku搜索
     *
     * @param querySkuBusinessListRequest
     * @return
     */
    @Override
    public ResultVO<PageVO<SkuBusinessListDTO>> getSkuBusinessList(QuerySkuBusinessListRequest querySkuBusinessListRequest) {
        PageVO<SkuBusinessListDTO> skuBusinessListList = packageManageFacade.getPackageSkuBusinessList(querySkuBusinessListRequest);
        return ResultVO.success(skuBusinessListList);
    }

}
