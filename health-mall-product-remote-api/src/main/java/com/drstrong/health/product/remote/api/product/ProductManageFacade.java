package com.drstrong.health.product.remote.api.product;

import com.drstrong.health.product.model.request.product.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品管理的 api 接口,目前主要提供给 cms 调用
 *
 * @author liuqiuyi
 * @date 2021/12/20 09:46
 */
@Api("健康商城-商品管理远程接口")
@FeignClient(value = "health-mall-product", path = "/product/manage")
public interface ProductManageFacade {
	@ApiOperation("根据后台分类,获取商品属性")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryId", value = "分类 id", dataType = "long", paramType = "query", required = true)
	})
	@GetMapping("/property/getById")
	ResultVO<List<CategoryAttributeItemVO>> getProperty(@Valid @NotNull(message = "categoryId 不能为空") @RequestParam("categoryId") Long categoryId);

	@ApiOperation(value = "商品上传或更新", notes = "如果 productId 为空,新增商品,如果不为空,更新商品")
	@PostMapping("/saveOrUpdate")
	ResultVO<ProductSaveResultVO> saveOrUpdateProduct(@RequestBody @Valid SaveProductRequest saveProductRequest);

	@ApiOperation("根据商品 id 查看商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "productId", value = "商品 id", dataType = "Long", paramType = "query", required = true)
	})
	@GetMapping("/getByProductId")
	ResultVO<ProductManageVO> getByProductId(@NotNull(message = "productId 不能为空") @RequestParam("productId") Long productId);

	@ApiOperation("分页查询 spu 信息")
	@PostMapping("/pageSpu")
	ResultVO<PageVO<ProductSpuVO>> pageSpu(@RequestBody QuerySpuRequest querySpuRequest);

	@ApiOperation("分页查询 sku 信息")
	@PostMapping("/pageSku")
	ResultVO<PageVO<ProductSkuVO>> pageSku(@RequestBody QuerySkuRequest querySkuRequest);

	@ApiOperation("分页查询 sku库存 信息")
	@PostMapping("/pageSkuStock")
	ResultVO<PageVO<ProductSkuStockVO>> pageSkuStock(@RequestBody QuerySkuStockRequest querySkuStockRequest);

	@ApiOperation("导出 sku库存 信息")
	@PostMapping("/skuStock/export")
	void exportSkuStock(@RequestBody QuerySkuStockRequest querySkuStockRequest, @RequestParam("request") HttpServletRequest request, @RequestParam("response") HttpServletResponse response);

	@ApiOperation("保存税收编码")
	@PostMapping("/revenue/add")
	ResultVO<Object> revenueAdd(@Valid @RequestBody AddRevenueRequest addRevenueRequest);
}
