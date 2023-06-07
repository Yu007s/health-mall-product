package com.drstrong.health.product.remote.api.medicine;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * 西药的远程接口
 *
 * @author zzw
 * @date 2023/6/7 16:41
 */
@Api("健康商城-安全用药-西药目录")
@FeignClient(value = "health-mall-product", path = "/inner/product/western/medicine")
public interface WesternMedicineRemoteApi {


}
