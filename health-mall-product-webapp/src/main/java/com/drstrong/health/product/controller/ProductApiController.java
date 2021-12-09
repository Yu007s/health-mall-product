package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.ProductDetailResponse;
import com.drstrong.health.product.model.response.product.ProductSearchResponse;
import com.drstrong.health.product.model.response.product.SkuBaseInfoResponse;
import com.drstrong.health.product.model.response.product.SpuBaseInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品 api controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:36
 */
@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = {"商品 api"}, description = "商品 api")
public class ProductApiController {

	@ApiOperation("商品搜索-获取商品名称列表(分页)")
	@GetMapping("/searchByName")
	public ResultVO<PageVO<SpuBaseInfoResponse>> pageSearchByName(ProductSearchRequest productSearchRequest) {

		return ResultVO.success();
	}

	@ApiOperation("商品搜索-获取搜索的商品列表(分页)")
	@GetMapping("/search/detail")
	public ResultVO<PageVO<ProductSearchResponse>> pageSearchDetail(ProductSearchRequest productSearchRequest) {

		return ResultVO.success();
	}

	@ApiOperation("根据 spuCode 查询 spu 信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu 编码", dataType = "string", paramType = "query", required = true)
	})
	@GetMapping("/spu/get")
	public ResultVO<ProductDetailResponse> getSpu(String spuCode) {

		return ResultVO.success();
	}

	@ApiOperation("根据 spuCode 查询所有的 sku 信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu 编码", dataType = "string", paramType = "query", required = true)
	})
	@GetMapping("/sku/listBySpuCode")
	public ResultVO<List<SkuBaseInfoResponse>> listSkuBySpuCode(String spuCode) {

		return ResultVO.success();
	}
}
