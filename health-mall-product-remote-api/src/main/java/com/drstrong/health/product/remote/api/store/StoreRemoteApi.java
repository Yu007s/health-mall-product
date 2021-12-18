package com.drstrong.health.product.remote.api.store;

import com.drstrong.health.product.remote.model.StorePostageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺运费远程服务api
 * @createTime 2021/12/15 15:54
 * @since TODO
 */
@Api(tags = "店铺相关API")
@FeignClient(value = "health-mall-product",path = "/store/api")
public interface StoreRemoteApi {

    @ApiOperation("批量获取店铺地区邮费")
    @GetMapping("getStorePostageByIds")
    List<StorePostageDTO> getStorePostageByIds(@RequestParam("storeIds") Set<Long> storeIds,@RequestParam("areaName") String areaName);
}
                                      