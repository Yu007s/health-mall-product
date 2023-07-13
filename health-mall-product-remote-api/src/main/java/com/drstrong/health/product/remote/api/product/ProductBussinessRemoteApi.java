package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.dto.product.ProductDetailInfoVO;
import com.drstrong.health.product.model.dto.product.ProductListInfoVO;
import com.drstrong.health.product.model.request.product.SearchWesternRequestParamBO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * huangpengp
 * 2023/7/11 21:08
 */
@Api("客户端药品远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/bussiness")
public interface ProductBussinessRemoteApi {

    @ApiOperation("空中药房的药品列表搜索")
    @PostMapping("/query/productList")
    ResultVO<List<ProductListInfoVO>> searchProductList(@RequestBody @Valid SearchWesternRequestParamBO searchWesternRequestParamBO);

    @ApiOperation("空中药房的查看药品详情")
    @PostMapping("/query/Detail")
    ResultVO<ProductDetailInfoVO> queryProductDetail(@RequestParam("skuCode") @Valid String skuCode);

}
