package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.model.request.store.AgencyStoreVO;
import com.drstrong.health.product.model.request.store.QueryStoreRequest;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 店铺的业务处理类，区分于之前给cms提供的接口，之后业务查询都使用这个类
 *
 * @author liuqiuyi
 * @date 2023/7/14 9:56
 */
@Api("健康商城-商品服务-店铺")
@FeignClient(value = "health-mall-product", path = "/inner/product/store")
public interface StoreBusinessRemoteApi {

    @ApiOperation("根据条件查询店铺")
    @PostMapping("/query")
    ResultVO<List<AgencyStoreVO>> queryStoreByParam(@RequestBody QueryStoreRequest queryStoreRequest);

}
