package com.drstrong.health.product.remote.api.chinese;

import com.drstrong.health.product.model.entity.chinese.ChineseMedicineEntity;
import com.drstrong.health.product.model.entity.chinese.OldChineseMedicine;
import com.drstrong.health.product.model.request.chinese.ChineseManagerSkuRequest;
import com.drstrong.health.product.model.request.chinese.QueryChineseMedicineRequest;
import com.drstrong.health.product.model.request.chinese.UpdateSkuStateRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.chinese.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * 健康商城-商品服务-中药管理页面远程接口
 *
 * @author liuqiuyi
 * @date 2022/8/1 10:46
 */
@Api("健康商城-商品服务-中药管理页面远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/chinese/manage")
public interface ChineseManagerRemoteApi {

    @ApiOperation("中药管理页面，列表查询")
    @PostMapping("/page/sku/info")
    ResultVO<PageVO<ChineseManagerSkuVO>> pageChineseManagerSku(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("中药管理页面，列表查询并导出")
    @PostMapping("/sku/info/export")
    ResultVO<List<ChineseManagerSkuVO>> listChineseManagerSkuExport(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("中药管理页面，关键字查询药材基础信息")
    @PostMapping("/keyword/search")
    ResultVO<List<ChineseMedicineResponse>> likeQueryChineseMedicine(@RequestBody QueryChineseMedicineRequest queryChineseMedicineRequest);

    @ApiOperation("中药管理页面，保存sku")
    @PostMapping("/sku/save")
    ResultVO<Object> saveOrUpdateSku(@RequestBody @Valid SaveOrUpdateSkuVO saveOrUpdateSkuVO);

    @ApiOperation("中药管理页面，根据skuCode获取sku详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "skuCode", value = "sku编码", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/get/sku")
    ResultVO<SaveOrUpdateSkuVO> getSkuByCode(@RequestParam("skuCode") @NotBlank(message = "入参不能为空") String skuCode);

    @ApiOperation("中药管理页面，sku批量上下架")
    @PostMapping("/update/sku/state")
    ResultVO<Object> listUpdateSkuState(@RequestBody @Valid UpdateSkuStateRequest updateSkuStateRequest);

    @ApiOperation("供应商中药库存页面，列表查询接口,提供给供应商远程调用")
    @PostMapping("/supplier/page/sku")
    ResultVO<PageVO<SupplierChineseManagerSkuVO>> pageSupplierChineseManagerSku(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("供应商中药库存页面，列表查询并导出,提供给供应商远程调用")
    @PostMapping("/supplier/sku/export")
    ResultVO<List<SupplierChineseManagerSkuVO>> listSupplierChineseManagerSkuExport(@RequestBody ChineseManagerSkuRequest skuRequest);

    @ApiOperation("查询店铺的供应商信息")
    @GetMapping("/store/supplier")
    ResultVO<List<SupplierBaseInfoVO>> getStoreSupplierInfo(@RequestParam("storeId") Long storeId, @RequestParam("medicineCode") String medicineCode);

    @ApiOperation("数据修复接口-获取所有的老表的中药材信息")
    @GetMapping("/list/oldChineseMedicine")
    ResultVO<List<OldChineseMedicine>> listOldChineseMedicine();

    @ApiOperation("数据修复接口-根据老的中药材 id 获取中药材信息")
    @PostMapping("/list/newChineseMedicine/ids")
    ResultVO<List<ChineseMedicineEntity>> listNewChineseMedicineByIds(@RequestBody Set<Long> medicineIds);
}
