package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.remote.model.ProductSkuInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * sku 远程接口
 *
 * @author liuqiuyi
 * @date 2021/12/14 17:22
 */
@Api(tags = "商品sku相关API")
@FeignClient(value = "health-mall-product",path = "/product/api")
public interface ProductSkuRemoteApi {
	/**
	 * 根据 skuId 集合,获取 sku 信息
	 *
	 * @param skuIds skuId 集合
	 * @return sku 信息
	 * @author liuqiuyi
	 * @date 2021/12/14 17:21
	 */
	@ApiOperation("根据 skuId 集合,获取 sku 信息")
	@GetMapping("getSkuBySkuIds")
	List<ProductSkuInfoDTO> getSkuInfoBySkuIds(@RequestParam("skuIds") List<Long> skuIds);
}
