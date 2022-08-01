package com.drstrong.health.product.remote.api.zStore;


import org.springframework.cloud.openfeign.FeignClient;


/**
 * @Author xieYueFeng
 * @Date 2022/07/26/11:55
 */
@FeignClient(value = "health-mall-product", path = "/product/zStore")
public interface StoreFacade {

}
