package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.SaveStorePostageRequest;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreQueryResponse;
import com.drstrong.health.product.model.response.store.v3.StorePostageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/15:22
 */
@Api("健康商城-商品服务-店铺管理")
@FeignClient(value = "health-mall-product", path = "/inner/product/chinese/store")
public interface StoreRemoteApi {

    /**
     * 新增店铺信息  做店铺名字重复校验  不允许店铺名字重复
     * @param store  店铺相关信息
     * @return 相应信息（成功、失败）
     */

    @ApiOperation("新增/修改店铺信息")
    @PostMapping("/save")
    ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailSaveRequest store);

    /**
     * 基于搜索条件获取店铺的基本信息
     * @param storeSearchRequest 店铺请求入参
     * @return 符合条件的店铺基本信息列表
     */
    @ApiOperation("获取符合条件的店铺基本信息列表")
    @PostMapping("/query")
    ResultVO<List<StoreInfoResponse>> queryStore(@RequestBody StoreSearchRequest storeSearchRequest);


    /**
     * 获取店铺详细信息
     *
     * @param storeId 店铺id
     * @return 店铺信息详情
     */
    @ApiOperation("获取店铺详细信息")
    @GetMapping("/queryById")
    ResultVO<StoreInfoEditResponse> queryStoreDetail(@RequestParam("storeId") @NotNull(message = "店铺id不能为空") Long storeId) ;


    /**
     * 店铺查询关联信息  查询所有需要信息
     *
     * @return 互联网医院名字集合  店铺类型名字集合
     */
    @ApiOperation("返回店铺关联的必要信息")
    @GetMapping("/queryInfo")
    ResultVO<StoreQueryResponse> queryStoreInfo() ;


    @ApiOperation("根据店铺 id 集合查询店铺基本信息")
    @PostMapping("/query/ids")
    ResultVO<List<StoreInfoResponse>> queryByIds(@RequestBody Set<Long> storeIds);

    @ApiOperation("根据店铺 id 查询店铺的配送费信息")
    @GetMapping("/query/postage")
    ResultVO<StorePostageVO> queryStorePostage(@RequestParam("storeId") @NotNull(message = "店铺id不能为空") Long storeId);

    @ApiOperation("保存店铺包邮金额")
    @PostMapping("/save/free-postage")
    ResultVO<Void> saveStorePostage(@RequestBody @Valid SaveStorePostageRequest saveStorePostageRequest);

    @ApiOperation("保存店铺下供应商的包邮金额")
    @PostMapping("/save/supplier-postage")
    ResultVO<Void> saveStoreSupplierPostage(@RequestBody @Valid SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest);
}
