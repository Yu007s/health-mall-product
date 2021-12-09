package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.request.product.AddRevenueRequest;
import com.drstrong.health.product.model.request.product.QuerySkuRequest;
import com.drstrong.health.product.model.request.product.QuerySpuRequest;
import com.drstrong.health.product.model.request.product.SaveProductRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductManageResponse;
import com.drstrong.health.product.model.response.product.ProductSkuResponse;
import com.drstrong.health.product.model.response.product.ProductSpuResponse;
import com.drstrong.health.product.model.response.product.PropertyInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation("根据后台分类,获取商品属性")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryId", value = "分类 id", dataType = "long", paramType = "query", required = true)
	})
	@GetMapping("/property/get")
	public ResultVO<List<PropertyInfoResponse>> getProperty(@NotNull(message = "categoryId 不能为空") Long categoryId) {

		return ResultVO.success();
	}

	@ApiOperation("商品上传")
	@PostMapping("/save")
	public ResultVO<Object> saveProperty(@RequestBody SaveProductRequest saveProductRequest) {

		return ResultVO.success();
	}

	@ApiOperation("根据 spuCode 查看商品信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu编码", dataType = "String", paramType = "query", required = true)
	})
	@GetMapping("/getBySpuCode")
	public ResultVO<List<ProductManageResponse>> getBySpuCode(@NotEmpty(message = "spuCode 不能为空") String spuCode) {

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
