package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.medicine.MedicineUsageDTO;
import com.drstrong.health.product.model.request.product.v3.ProductManageQueryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.v3.AgreementSkuInfoVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("分页查询sku信息（不包括中药）")
    @PostMapping({"/page/query"})
    ResultVO<PageVO<AgreementSkuInfoVO>> pageQuerySkuInfo(@RequestBody ProductManageQueryRequest productManageQueryRequest);
}
