package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.product.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.ProductManageFacade;
import com.drstrong.health.product.service.CategoryAttributeService;
import com.drstrong.health.product.service.ProductBasicsInfoService;
import com.drstrong.health.product.service.ProductSkuRevenueService;
import com.drstrong.health.product.service.ProductSkuService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 商品 controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 13:56
 */
@Validated
@RestController
@RequestMapping("/product/manage")
@Slf4j
@Api(tags = {"商品管理"}, description = "商品管理")
public class ProductManageController implements ProductManageFacade {
	@Resource
	CategoryAttributeService categoryAttributeService;

	@Resource
	ProductBasicsInfoService productBasicsInfoService;

	@Resource
	ProductSkuRevenueService productSkuRevenueService;

	@Resource
	ProductSkuService productSkuService;

	@Override
	public ResultVO<List<CategoryAttributeItemVO>> getProperty(@Valid @NotNull(message = "categoryId 不能为空") Long categoryId) {
		List<CategoryAttributeItemVO> categoryAttributeServiceItems = categoryAttributeService.getItems(categoryId);
		return ResultVO.success(categoryAttributeServiceItems);
	}

	@Override
	public ResultVO<ProductSaveResultVO> saveOrUpdateProduct(@RequestBody @Valid SaveProductRequest saveProductRequest) {
		Long productId = productBasicsInfoService.saveOrUpdateProduct(saveProductRequest);
		return ResultVO.success(new ProductSaveResultVO(productId));
	}

	@Override
	public ResultVO<ProductManageVO> getByProductId(@NotNull(message = "productId 不能为空") Long productId) {
		ProductManageVO productManageVO = productBasicsInfoService.queryManageProductInfo(productId);
		return ResultVO.success(productManageVO);
	}

	@Override
	public ResultVO<PageVO<ProductSpuVO>> pageSpu(QuerySpuRequest querySpuRequest) {
		PageVO<ProductSpuVO> resultPageVO = productBasicsInfoService.pageQuerySpuByParam(querySpuRequest);
		return ResultVO.success(resultPageVO);
	}

	@Override
	public ResultVO<PageVO<ProductSkuVO>> pageSku(QuerySkuRequest querySkuRequest) {
		PageVO<ProductSkuVO> resultPageVO = productSkuService.pageQuerySkuByParam(querySkuRequest);
		return ResultVO.success(resultPageVO);
	}

	@Override
	public ResultVO<PageVO<ProductSkuStockVO>> pageSkuStore(QuerySkuStockRequest querySkuStockRequest) {
		PageVO<ProductSkuStockVO> resultPageVO = productSkuService.pageQuerySkuStockByParam(querySkuStockRequest);
		return ResultVO.success(resultPageVO);
	}

	@Override
	public void exportSkuStock(QuerySkuStockRequest querySkuStockRequest, HttpServletRequest request, HttpServletResponse response) {
		productSkuService.exportSkuStock(querySkuStockRequest,request,response);
	}

	@Override
	public ResultVO<Object> revenueAdd(@Valid @RequestBody AddRevenueRequest addRevenueRequest) {
		productSkuRevenueService.revenueAdd(addRevenueRequest);
		return ResultVO.success();
	}
}
