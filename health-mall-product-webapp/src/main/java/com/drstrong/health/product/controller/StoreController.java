package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.model.request.store.*;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.store.StoreInfoResponse;
import com.drstrong.health.product.model.response.store.StoreSkuResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation("获取所有的店铺信息")
	@GetMapping("/query")
	public ResultVO<List<StoreInfoResponse>> frontQuery(StoreRequest storeRequest) {

		return ResultVO.success();
	}

	@ApiOperation("添加店铺")
	@PostMapping("/add")
	public ResultVO<Object> add(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest) {

		return ResultVO.success();
	}

	@ApiOperation("更新店铺")
	@PostMapping("/update")
	public ResultVO<Object> update(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest) {

		return ResultVO.success();
	}

	@ApiOperation("禁用店铺")
	@PostMapping("/updateState")
	public ResultVO<Object> updateState(@RequestBody @Valid StoreAddOrUpdateRequest storeAddOrUpdateRequest) {

		return ResultVO.success();
	}

	@ApiOperation("查询店铺的配送费")
	@GetMapping("/postage/get")
	public ResultVO<StorePostageResponse> getPostage(Long storeId) {

		return ResultVO.success();
	}

	@ApiOperation("更新店铺的配送费")
	@PostMapping("/postage/update")
	public ResultVO<Object> updatePostage(@RequestBody UpdatePostageRequest updatePostageRequest) {

		return ResultVO.success();
	}

	@ApiOperation("分页查询店铺的 sku 列表")
	@GetMapping("/sku/page")
	public ResultVO<PageVO<StoreSkuResponse>> pageSkuList(StoreSkuRequest storeSkuRequest) {

		return ResultVO.success();
	}

	@ApiOperation("修改三方的进货单价")
	@PostMapping("/purchasePrice/update")
	public ResultVO<Object> updatePurchasePrice(@RequestBody UpdateThreeRequest updateThreeRequest) {

		return ResultVO.success();
	}

	@ApiOperation("关联药店商品")
	@PostMapping("/relevance/add")
	public ResultVO<Object> relevanceAdd(@RequestBody UpdateThreeRequest updateThreeRequest) {

		return ResultVO.success();
	}

	@ApiOperation("上架/下架 合作商品")
	@PostMapping("/sku/updateState")
	public ResultVO<Object> updateSkuState(@RequestBody UpdateSkuRequest updateSkuRequest) {

		return ResultVO.success();
	}


}
