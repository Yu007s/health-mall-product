package com.drstrong.health.product.remote.api.store;
import com.drstrong.health.product.model.request.store.StoreInfoDetailSaveRequest;
import com.drstrong.health.product.model.request.store.StoreSearchRequest;
import com.drstrong.health.product.model.response.area.ProvinceAreaInfo;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.response.store.StoreAddResponse;
import com.drstrong.health.product.model.response.store.StoreInfoEditResponse;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreQueryResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/15:22
 */
@Api("健康商城-商品服务-店铺管理")
@FeignClient(value = "health-mall-product", path = "/product/chinese/store")
public interface StoreRemoteApi {
    @ApiOperation("新增/修改店铺信息")
    @PostMapping("/save")
    ResultVO<String> savaStore(@RequestBody @Valid StoreInfoDetailSaveRequest store, @RequestParam Long userId) throws Exception ;


    @ApiOperation("获取符合条件的店铺基本信息列表")
    @GetMapping("/query")
    ResultVO<List<StoreInfoResponse>> queryStore(StoreSearchRequest storeSearchRequest) ;


    @ApiOperation("获取店铺详细信息")
    @GetMapping("/queryById")
    ResultVO<StoreInfoEditResponse> queryStoreDetail(@RequestParam Long storeId) ;


    @ApiOperation("增加店铺时查找相应的信息")
    @GetMapping("/queryAddInfo")
    ResultVO<StoreAddResponse> queryAddStoreInfo() ;


    @ApiOperation("增加店铺时查找相应的信息")
    @GetMapping("/queryInfo")
    ResultVO<StoreQueryResponse> queryStoreInfo() ;


    @ApiOperation("查询所有的城市")
    @GetMapping("/queryAll")
    ResultVO<List<ProvinceAreaInfo>> queryAll() ;


    @ApiOperation("根据供应商id查询关联的店铺信息")
    @GetMapping("/searchStore")
    List<StoreInfoResponse> queryStoreBySupplierId(@RequestParam Long supplierId);
}
