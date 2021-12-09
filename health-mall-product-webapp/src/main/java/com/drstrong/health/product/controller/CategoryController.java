package com.drstrong.health.product.controller;

import cn.strong.common.base.Result;
import com.drstrong.health.product.model.request.category.AddFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryIdRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.request.category.UpdateFrontCategoryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryResponse;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;
import com.drstrong.health.product.service.FrontCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品分类 controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 09:38
 */
@RestController
@RequestMapping("/product/category")
@Slf4j
@Api(tags = {"商品分类"}, description = "商品分类")
public class CategoryController {
	@Resource
	FrontCategoryService frontCategoryService;

	@ApiOperation(value = "获取所有前台分类", notes = "前台分类较少,前后端讨论后决定不进行分页查询")
	@GetMapping("/front/query")
	public Result<List<FrontCategoryResponse>> frontQuery(CategoryQueryRequest categoryQueryRequest) {
		List<FrontCategoryResponse> frontCategoryResponses = frontCategoryService.queryAll();
		return Result.ok(frontCategoryResponses);
	}

	@ApiOperation("获取所有后台分类")
	@GetMapping("/back/query")
	public Result<List<BackCategoryResponse>> backQuery(CategoryQueryRequest categoryQueryRequest) {

		return Result.ok();
	}

	@ApiOperation("添加前台分类")
	@PostMapping("/front/add")
	public Result<Object> addFront(@RequestBody @Valid AddFrontCategoryRequest addFrontCategoryRequest) {

		return Result.ok();
	}

	@ApiOperation("更新前台分类")
	@PostMapping("/front/update")
	public Result<Object> updateFront(@RequestBody @Valid UpdateFrontCategoryRequest updateFrontCategoryRequest) {

		return Result.ok();
	}

	@ApiOperation("更新分类状态")
	@PostMapping("/front/updateState")
	public Result<Object> updateStateFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {

		return Result.ok();
	}

	@ApiOperation("删除分类信息")
	@PostMapping("/front/delete")
	public Result<Object> deleteFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {

		return Result.ok();
	}
}
