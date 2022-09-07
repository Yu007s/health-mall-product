package com.drstrong.health.product.controller.product;

import com.drstrong.health.product.model.request.product.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.product.*;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.product.ProductManageFacade;
import com.drstrong.health.product.service.category.CategoryAttributeItemService;
import com.drstrong.health.product.service.product.ProductManageService;
import com.drstrong.health.product.service.product.ProductSkuManageService;
import com.drstrong.health.product.service.product.ProductSkuRevenueService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/inner/product/manage")
@Slf4j
@Api(tags = {"cms-商品管理"})
@Deprecated
public class ProductManageController implements ProductManageFacade {
	@Resource
	ProductManageService productManageService;

	@Resource
	ProductSkuManageService productSkuManageService;

	@Resource
	CategoryAttributeItemService categoryAttributeItemService;

	@Resource
	ProductSkuRevenueService productSkuRevenueService;

	@Override
	public ResultVO<List<CategoryAttributeItemVO>> getProperty(@Valid @NotNull(message = "categoryId 不能为空") Long categoryId) {
//		List<CategoryAttributeItemVO> categoryAttributeServiceItems = categoryAttributeItemService.getItems(categoryId);
//		return ResultVO.success(categoryAttributeServiceItems);
		return ResultVO.success(Lists.newArrayList());
	}

	@Override
	public ResultVO<ProductSaveResultVO> saveOrUpdateProduct(@RequestBody @Valid SaveProductRequest saveProductRequest) {
//		Long productId = productManageService.saveOrUpdateProduct(saveProductRequest);
//		return ResultVO.success(new ProductSaveResultVO(productId));
		return ResultVO.success(new ProductSaveResultVO(-1L));
	}

	@Override
	public ResultVO<ProductManageVO> getByProductId(@NotNull(message = "productId 不能为空") Long productId) {
//		ProductManageVO productManageVO = productManageService.queryManageProductInfo(productId);
//		return ResultVO.success(productManageVO);
		return ResultVO.success(new ProductManageVO());
	}

	@Override
	public ResultVO<PageVO<ProductSpuVO>> pageSpu(QuerySpuRequest querySpuRequest) {
//		PageVO<ProductSpuVO> resultPageVO = productManageService.pageQuerySpuByParam(querySpuRequest);
//		return ResultVO.success(resultPageVO);
		PageVO<ProductSpuVO> objectPageVO = PageVO.newBuilder().pageNo(querySpuRequest.getPageNo()).pageSize(querySpuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		return ResultVO.success(objectPageVO);
	}

	@Override
	public ResultVO<PageVO<ProductSkuVO>> pageSku(QuerySkuRequest querySkuRequest) {
//		PageVO<ProductSkuVO> resultPageVO = productSkuManageService.pageQuerySkuByParam(querySkuRequest);
//		return ResultVO.success(resultPageVO);
		PageVO<ProductSkuVO> objectPageVO = PageVO.newBuilder().pageNo(querySkuRequest.getPageNo()).pageSize(querySkuRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		return ResultVO.success(objectPageVO);
	}

	@Override
	public ResultVO<PageVO<ProductSkuStockVO>> pageSkuStock(QuerySkuStockRequest querySkuStockRequest) {
//		PageVO<ProductSkuStockVO> resultPageVO = productSkuManageService.pageQuerySkuStockByParam(querySkuStockRequest);
//		return ResultVO.success(resultPageVO);
		PageVO<ProductSkuStockVO> objectPageVO = PageVO.newBuilder().pageNo(querySkuStockRequest.getPageNo()).pageSize(querySkuStockRequest.getPageSize()).totalCount(0).result(Lists.newArrayList()).build();
		return ResultVO.success(objectPageVO);
	}

	@Override
	public ResultVO<List<ProductSkuStockVO>> searchSkuStock(QuerySkuStockRequest querySkuStockRequest) {
//		List<ProductSkuStockVO> productSkuStockVOS = productSkuManageService.searchSkuStock(querySkuStockRequest);
//		return ResultVO.success(productSkuStockVOS);
		return ResultVO.success(Lists.newArrayList());
	}

	@Override
	public ResultVO<Object> revenueAdd(@Valid @RequestBody AddRevenueRequest addRevenueRequest) {
//		productSkuRevenueService.revenueAdd(addRevenueRequest);
		return ResultVO.success();
	}
}
