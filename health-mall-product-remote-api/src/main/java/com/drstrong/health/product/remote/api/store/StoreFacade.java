package com.drstrong.health.product.remote.api.store;


import org.springframework.cloud.openfeign.FeignClient;


/**
 * @Author xieYueFeng
 * @Date 2022/07/26/11:55
 */
@FeignClient(value = "health-mall-product", path = "/product/chineseStore")
public interface StoreFacade {

}
