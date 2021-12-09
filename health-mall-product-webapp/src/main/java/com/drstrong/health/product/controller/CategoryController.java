package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.category.AddFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryIdRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.request.category.UpdateFrontCategoryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryResponse;
import com.drstrong.health.product.model.response.category.FrontCategoryResponse;
import com.drstrong.health.product.model.response.result.ResultVO;
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
	public ResultVO<List<FrontCategoryResponse>> frontQuery(CategoryQueryRequest categoryQueryRequest) {
		List<FrontCategoryResponse> frontCategoryResponses = frontCategoryService.queryByParam(categoryQueryRequest);
		return ResultVO.success(frontCategoryResponses);
	}

	@ApiOperation("获取所有后台分类")
	@GetMapping("/back/query")
	public ResultVO<List<BackCategoryResponse>> backQuery(CategoryQueryRequest categoryQueryRequest) {

		return ResultVO.success();
	}

	@ApiOperation("添加前台分类")
	@PostMapping("/front/add")
	public ResultVO<Object> addFront(@RequestBody @Valid AddFrontCategoryRequest addFrontCategoryRequest) {

		return ResultVO.success();
	}

	@ApiOperation("更新前台分类")
	@PostMapping("/front/update")
	public ResultVO<Object> updateFront(@RequestBody @Valid UpdateFrontCategoryRequest updateFrontCategoryRequest) {

		return ResultVO.success();
	}

	@ApiOperation("更新分类状态")
	@PostMapping("/front/updateState")
	public ResultVO<Object> updateStateFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {

		return ResultVO.success();
	}

	@ApiOperation("删除分类信息")
	@PostMapping("/front/delete")
	public ResultVO<Object> deleteFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {

		return ResultVO.success();
	}
}
