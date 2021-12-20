package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryIdRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.BackCategoryService;
import com.drstrong.health.product.service.FrontCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

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
public class CategoryManageController {
	@Resource
	FrontCategoryService frontCategoryService;

	@Resource
	BackCategoryService backCategoryService;

	// TODO 获取当前登录信息

	@ApiOperation(value = "获取所有前台分类", notes = "前台分类较少,前后端讨论后决定不进行分页查询")
	@GetMapping("/front/query")
	public ResultVO<List<FrontCategoryVO>> frontQuery(CategoryQueryRequest categoryQueryRequest) {
		List<FrontCategoryVO> frontCategoryVoList = frontCategoryService.queryByParamToTree(categoryQueryRequest);
		return ResultVO.success(frontCategoryVoList);
	}

	@ApiOperation(value = "获取所有后台分类", notes = "后台分类较少,前后端讨论后决定不进行分页查询")
	@GetMapping("/back/query")
	public ResultVO<List<BackCategoryVO>> backQuery(CategoryQueryRequest categoryQueryRequest) {
		List<BackCategoryVO> backCategoryVOList = backCategoryService.queryByParamToTree(categoryQueryRequest);
		return ResultVO.success(backCategoryVOList);
	}

	@ApiOperation("添加前台分类")
	@PostMapping("/front/add")
	public ResultVO<Object> addFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest addOrUpdateFrontCategoryRequest) {
		addOrUpdateFrontCategoryRequest.setUserId("999");
		frontCategoryService.add(addOrUpdateFrontCategoryRequest);
		return ResultVO.success();
	}

	@ApiOperation("更新前台分类")
	@PostMapping("/front/update")
	public ResultVO<Object> updateFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest) {
		if (Objects.isNull(updateFrontCategoryRequest.getCategoryId())) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		updateFrontCategoryRequest.setUserId("999");
		frontCategoryService.update(updateFrontCategoryRequest);
		return ResultVO.success();
	}

	@ApiOperation("更新分类状态")
	@PostMapping("/front/updateState")
	public ResultVO<Object> updateStateFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {
		frontCategoryService.updateStateFront(categoryIdRequest.getCategoryId(), "999");
		return ResultVO.success();
	}

	@ApiOperation("删除分类信息")
	@PostMapping("/front/delete")
	public ResultVO<Object> deleteFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {
		frontCategoryService.deleteFrontCategoryById(categoryIdRequest.getCategoryId(), "999");
		return ResultVO.success();
	}
}
