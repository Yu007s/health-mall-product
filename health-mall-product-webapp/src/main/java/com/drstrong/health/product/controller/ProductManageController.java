package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.product.AddRevenueRequest;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.CategoryAttributeItemVO;
import com.drstrong.health.product.model.response.product.ProductManageVO;
import com.drstrong.health.product.model.response.product.ProductSkuResponse;
import com.drstrong.health.product.model.response.product.ProductSpuResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.CategoryAttributeService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品 controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 13:56
 */
@RestController
@RequestMapping("/product/manage")
@Slf4j
@Api(tags = {"商品管理"}, description = "商品管理")
public class ProductManageController {
	@Resource
	CategoryAttributeService categoryAttributeService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@ApiOperation("根据后台分类,获取商品属性")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryId", value = "分类 id", dataType = "long", paramType = "query", required = true)
	})
	@GetMapping("/property/getById")
	public ResultVO<List<CategoryAttributeItemVO>> getProperty(@Valid @NotNull(message = "categoryId 不能为空") Long categoryId) {
		List<CategoryAttributeItemVO> categoryAttributeServiceItems = categoryAttributeService.getItems(categoryId);
		return ResultVO.success(categoryAttributeServiceItems);
	}

	@ApiOperation("商品上传")
	@PostMapping("/saveOrUpdate")
	public ResultVO<Object> saveProperty(@RequestBody @Valid SaveProductRequest saveProductRequest) {
		productBasicsInfoService.saveOrUpdateProduct(saveProductRequest);
		return ResultVO.success();
	}

	@ApiOperation("根据商品 id 查看商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu编码", dataType = "String", paramType = "query", required = true)
	})
	@GetMapping("/getByProductId")
	public ResultVO<List<ProductManageVO>> getByProductId(@NotEmpty(message = "productId 不能为空") Long productId) {

		return ResultVO.success();
	}

	@ApiOperation("分页查询 spu 信息")
	@GetMapping("/pageSpu")
	public ResultVO<PageVO<ProductSpuResponse>> pageSpu(QuerySpuRequest querySpuRequest) {

		return ResultVO.success();
	}

	@ApiOperation("分页查询 sku 信息")
	@GetMapping("/pageSku")
	public ResultVO<PageVO<ProductSkuResponse>> pageSku(QuerySkuRequest querySkuRequest) {

		return ResultVO.success();
	}

	@ApiOperation("保存税收编码")
	@PostMapping("/revenue/add")
	public ResultVO<Object> revenueAdd(@RequestBody AddRevenueRequest addRevenueRequest) {

		return ResultVO.success();
	}
}
