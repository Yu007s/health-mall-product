package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.remote.model.StorePostageDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺运费远程服务api
 * @createTime 2021/12/15 15:54
 * @since TODO
 */
@FeignClient(value = "health-mall-product",path = "/product/store")
public interface StoreRemoteApi {

    @ApiOperation("获取所有的店铺信息")
    @GetMapping("/query")
    ResultVO<List<StoreInfoResponse>> queryAllStore();

    @ApiOperation("添加店铺")
    @PostMapping("/add")
    ResultVO<Object> add(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest, @RequestParam("userId") String userId);

    @ApiOperation("更新店铺")
    @PostMapping("/update")
    ResultVO<Object> update(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest,@RequestParam("userId") String userId);

    @ApiOperation("禁用店铺")
    @PostMapping("/updateState")
    ResultVO<Object> updateState(@RequestBody @Valid StoreIdRequest storeIdRequest,@RequestParam("userId") String userId);

    @ApiOperation("查询店铺的配送费")
    @GetMapping("/postage/get")
    ResultVO<StorePostage> getPostage(@RequestParam("storeId") Long storeId);

    @ApiOperation("更新店铺的配送费")
    @PostMapping("/postage/update")
    ResultVO<Object> updatePostage(@RequestBody StorePostage storePostage,@RequestParam("userId") String userId);

    @ApiOperation("分页查询店铺的 sku 列表")
    @PostMapping("/sku/page")
    ResultVO<PageVO<StoreSkuResponse>> pageSkuList(@RequestBody StoreSkuRequest storeSkuRequest);

    @ApiOperation("修改三方的进货单价")
    @PostMapping("/purchasePrice/update")
    ResultVO<Object> updatePurchasePrice(@RequestBody UpdateThreeRequest updateThreeRequest,@RequestParam("userId") String userId);

    @ApiOperation("关联药店商品")
    @PostMapping("/relevance/add")
    ResultVO<Object> relevanceAdd(@RequestBody RelevanceThreeRequest relevanceThreeRequest,@RequestParam("userId") String userId);

    @ApiOperation("上架/下架 合作商品")
    @PostMapping("/sku/updateState")
    ResultVO<Object> updateSkuState(@RequestBody UpdateSkuRequest updateSkuRequest,@RequestParam("userId")String userId);

    @ApiOperation("商品维护列表导出")
    @PostMapping("/sku/export")
    void exportSku(@RequestBody StoreSkuRequest storeSkuRequest, HttpServletRequest request, HttpServletResponse response);

    @ApiOperation("批量获取店铺地区邮费")
    @GetMapping("getStorePostageByIds")
    List<StorePostageDTO> getStorePostageByIds(@RequestParam("storeIds") Set<Long> storeIds,@RequestParam("areaName") String areaName);
}
                                      