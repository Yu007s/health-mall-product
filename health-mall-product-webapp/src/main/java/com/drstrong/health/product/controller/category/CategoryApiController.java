package com.drstrong.health.product.controller.category;

import com.drstrong.health.product.model.request.category.PageCategoryIdRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.CategoryProductVO;
import com.drstrong.health.product.model.response.category.HomeCategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.category.FrontCategoryService;
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
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 小程序商品分类 controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 09:38
 */
@Validated
@RestController
@RequestMapping("/product/category")
@Slf4j
@Api(tags = {"小程序-商品分类"})
public class CategoryApiController {
	@Resource
	FrontCategoryService frontCategoryService;

	@ApiOperation("小程序 - 首页获取分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "level", value = "查询的前台分类层级,1-表示查询一级分类,2-表示查询一级分类和二级分类,不传默认查询一级分类", dataType = "int", paramType = "query")
	})
	@GetMapping("/home/get")
	public ResultVO<List<HomeCategoryVO>> getHomeCategory(@Valid @Max(value = 2, message = "分类层级最大为 2") Integer level) {
		List<HomeCategoryVO> homeCategoryVOList = frontCategoryService.getHomeCategory(level);
		return ResultVO.success(homeCategoryVOList);
	}

	@ApiOperation(value = "小程序 - 根据一级分类id 获取二级分类信息", notes = "考虑到小程序性能问题,和前端约定单独提供一个根据一级分类查询二级分类信息的接口")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "categoryId", value = "一级分类 id", dataType = "long", paramType = "query", required = true)
	})
	@GetMapping("/levelTwo/getById")
	public ResultVO<List<HomeCategoryVO>> getLevelTwoById(@Valid @NotNull(message = "一级分类不能为空") Long categoryId) {
		List<HomeCategoryVO> homeCategoryVOList = frontCategoryService.getLevelTwoById(categoryId);
		return ResultVO.success(homeCategoryVOList);
	}


	@ApiOperation("小程序 - 根据分类 id 获取商品列表(分页)")
	@GetMapping("/page/queryById")
	public ResultVO<PageVO<CategoryProductVO>> pageCategoryProduct(@Valid PageCategoryIdRequest pageCategoryIdRequest) {
		PageVO<CategoryProductVO> categoryPageVO = frontCategoryService.pageCategoryProduct(pageCategoryIdRequest);
		return ResultVO.success(categoryPageVO);
	}
}
