package com.drstrong.health.product.remote.api.chinese;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author xieYueFeng
 * @Date 2022/07/27/10:22
 */
@FeignClient(value = "health-mall-product", path = "/product/chineseMedicine")
public interface ChineseMedicineFacade {

}
