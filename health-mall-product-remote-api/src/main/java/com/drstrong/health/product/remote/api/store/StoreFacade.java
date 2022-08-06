package com.drstrong.health.product.remote.api.store;


import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @Author xieYueFeng
 * @Date 2022/07/26/11:55
 */
@FeignClient(value = "health-mall-product", path = "/product/chinese/store")
public interface StoreFacade {
    /**
     * 根据供应商id查询关联的店铺信息
     * @param supplierId 供应商id
     * @return 所有符合要求的店铺
     */
    @ApiOperation("根据供应商id查询关联的店铺信息")
    @GetMapping("/searchStore")
    List<StoreInfoResponse> queryStoreBySupplierId(@RequestParam("supplierId") @NotNull(message = "供应商id不能为null") Long supplierId) ;
}
