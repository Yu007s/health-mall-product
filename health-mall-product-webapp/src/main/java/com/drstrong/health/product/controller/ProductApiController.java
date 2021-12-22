package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.PageRequest;
import com.drstrong.health.product.model.request.product.ProductSearchRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.ProductAttributeService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductSkuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * 商品 api controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:36
 */
@Validated
@RestController
@RequestMapping("/product")
@Slf4j
@Api(tags = {"小程序-商品 api"}, description = "小程序-商品 api")
public class ProductApiController {
	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductSkuService productSkuService;

	@Resource
	ProductAttributeService productAttributeService;

	@ApiOperation("商品搜索-获取商品名称列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "content", value = "搜索内容", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "count", value = "返回数据条数(不传默认为 10 条)", dataType = "int", paramType = "query")
	})
	@GetMapping("/searchByName")
	public ResultVO<SearchNameResultVO> pageSearchByName(@NotBlank(message = "搜索内容不能为空") String content, Integer count) {
		if (Objects.isNull(count)) {
			count = 10;
		}
		List<String> resultList = productBasicsInfoService.pageSearchByName(content, count);
		return ResultVO.success(new SearchNameResultVO(resultList));
	}

	@ApiOperation("商品搜索-获取搜索的商品列表(分页)")
	@GetMapping("/search/detail")
	public ResultVO<PageVO<ProductSearchDetailVO>> pageSearchDetail(ProductSearchRequest productSearchRequest) {
		PageVO<ProductSearchDetailVO> resultList = productBasicsInfoService.pageSearchDetail(productSearchRequest);
		return ResultVO.success(resultList);
	}

	@ApiOperation("根据 spuCode 查询 spu 信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu 编码", dataType = "string", paramType = "query", required = true)
	})
	@GetMapping("/spu/get")
	public ResultVO<ProductDetailVO> getSpu(@NotBlank(message = "spuCode 不能为空") String spuCode) {
		ProductDetailVO productDetailVO = productBasicsInfoService.getSpuInfo(spuCode);
		return ResultVO.success(productDetailVO);
	}

	@ApiOperation("根据 spuCode 查询所有的 sku 信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu 编码", dataType = "string", paramType = "query", required = true)
	})
	@GetMapping("/sku/listBySpuCode")
	public ResultVO<List<SkuBaseInfoVO>> listSkuBySpuCode(@NotBlank(message = "spuCode 不能为空") String spuCode) {
		List<SkuBaseInfoVO> skuBaseInfoVOList = productSkuService.listSkuBySpuCode(spuCode);
		return ResultVO.success(skuBaseInfoVOList);
	}

	@ApiOperation("根据 spuCode 查询商品的属性信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "spuCode", value = "spu 编码", dataType = "string", paramType = "query", required = true)
	})
	@GetMapping("/property/get")
	public ResultVO<List<ProductPropertyVO>> getPropertyByCode(@NotBlank(message = "spuCode 不能为空") String spuCode) {
		List<ProductPropertyVO> productPropertyVOList = productAttributeService.getPropertyByCode(spuCode);
		return ResultVO.success(productPropertyVOList);
	}

	@ApiOperation("热门推荐商品分页展示")
	@GetMapping("/search/recommend")
	public ResultVO<PageVO<ProductRecommendVO>> pageSearchRecommend(PageRequest pageRequest) {
		PageVO<ProductRecommendVO> resultList = productBasicsInfoService.pageSearchRecommend(pageRequest);
		return ResultVO.success(resultList);
	}

}
