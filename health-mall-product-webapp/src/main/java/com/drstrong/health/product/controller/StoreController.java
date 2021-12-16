package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import com.drstrong.health.product.service.StoreService;
import com.drstrong.health.product.service.StoreThreeRelevanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 店铺管理
 *
 * @author liuqiuyi
 * @date 2021/12/7 09:51
 */
@RestController
@RequestMapping("/product/store")
@Slf4j
@Api(tags = {"店铺管理"}, description = "店铺管理")
public class StoreController {

	@Resource
	private StoreService storeService;
	@Resource
	private StoreThreeRelevanceService storeThreeRelevanceService;

	@ApiOperation("获取所有的店铺信息")
	@GetMapping("/query")
	public ResultVO<List<StoreInfoResponse>> frontQuery() {
		List<StoreInfoResponse> storeInfoResponses = storeService.queryAll();
		return ResultVO.success(storeInfoResponses);
	}

	@ApiOperation("添加店铺")
	@PostMapping("/add")
	public ResultVO<Object> add(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest) {
		//TODO 获取当前操作人
		storeService.add(storeAddOrUpdateRequest,888L);
		return ResultVO.success();
	}

	@ApiOperation("更新店铺")
	@PostMapping("/update")
	public ResultVO<Object> update(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest) {
		//TODO 获取当前操作人
		storeService.update(storeAddOrUpdateRequest,888L);
		return ResultVO.success();
	}

	@ApiOperation("禁用店铺")
	@PostMapping("/updateState")
	public ResultVO<Object> updateState(@RequestBody @Valid StoreIdRequest storeIdRequest) {
		//TODO 获取当前操作人
		storeService.disable(storeIdRequest,888L);
		return ResultVO.success();
	}

	@ApiOperation("查询店铺的配送费")
	@GetMapping("/postage/get")
	public ResultVO<StorePostage> getPostage(Long storeId) {
		StorePostage storePostage = storeService.getPostage(storeId);
		return ResultVO.success(storePostage);
	}

	@ApiOperation("更新店铺的配送费")
	@PostMapping("/postage/update")
	public ResultVO<Object> updatePostage(@RequestBody StorePostage storePostage) {
		//TODO 获取当前操作人
		storeService.updatePostage(storePostage,888L);
		return ResultVO.success();
	}

	@ApiOperation("分页查询店铺的 sku 列表")
	@GetMapping("/sku/page")
	public ResultVO<PageVO<StoreSkuResponse>> pageSkuList(StoreSkuRequest storeSkuRequest) {
		PageVO<StoreSkuResponse> pageVO = storeThreeRelevanceService.pageSkuList(storeSkuRequest);
		return ResultVO.success(pageVO);
	}

	@ApiOperation("修改三方的进货单价")
	@PostMapping("/purchasePrice/update")
	public ResultVO<Object> updatePurchasePrice(@RequestBody UpdateThreeRequest updateThreeRequest) {
		//TODO 获取当前操作人
		storeThreeRelevanceService.updatePurchasePrice(updateThreeRequest,888L);
		return ResultVO.success();
	}

	@ApiOperation("关联药店商品")
	@PostMapping("/relevance/add")
	public ResultVO<Object> relevanceAdd(@RequestBody RelevanceThreeRequest relevanceThreeRequest) {
		//TODO 获取当前操作人
		storeThreeRelevanceService.relevanceAdd(relevanceThreeRequest,888L);
		return ResultVO.success();
	}

	@ApiOperation("上架/下架 合作商品")
	@PostMapping("/sku/updateState")
	public ResultVO<Object> updateSkuState(@RequestBody UpdateSkuRequest updateSkuRequest) {
		//TODO 获取当前操作人
		storeThreeRelevanceService.updateSkuState(updateSkuRequest,888L);
		return ResultVO.success();
	}


}
