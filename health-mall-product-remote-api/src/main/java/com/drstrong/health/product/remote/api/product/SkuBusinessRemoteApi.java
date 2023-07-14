package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/7/14 15:29
 */
@Api("健康商城-商品服务-sku业务处理远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/sku/business")
public interface SkuBusinessRemoteApi {

    @ApiModelProperty("查询sku的用法用量")
    @PostMapping("/query/sku/usage")
    ResultVO<List<MedicineUsageDTO>> queryMedicineUsageBySkuCode(@RequestBody Set<String> skuCodes);
}
