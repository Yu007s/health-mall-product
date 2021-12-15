package com.drstrong.health.product.controller;

import com.drstrong.health.product.model.request.category.PageCategoryIdRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.HomeCategoryVO;
import com.drstrong.health.product.model.response.product.ProductSpuVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.service.FrontCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;

/**
 * 小程序商品分类 controller
 *
 * @author liuqiuyi
 * @date 2021/12/6 09:38
 */
@RestController
@RequestMapping("/product/category")
@Slf4j
@Api(tags = {"小程序-商品分类"}, description = "小程序-商品分类")
public class CategoryApiController {
	@Resource
	FrontCategoryService frontCategoryService;

	@ApiOperation("小程序-获取分类信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "level", value = "查询的前台分类层级,1-表示查询一级分类,2-表示查询一级分类和二级分类,不传默认查询一级分类", dataType = "int", paramType = "query")
	})
	@GetMapping("/home/get")
	public ResultVO<List<HomeCategoryVO>> getHomeCategory(@Valid @Max(value = 2, message = "分类层级最大为 2") Integer level) {
		List<HomeCategoryVO> homeCategoryVOList = frontCategoryService.getHomeCategory(level);
		return ResultVO.success(homeCategoryVOList);
	}

	@ApiOperation("小程序-根据分类 id 获取商品列表(分页)")
	@GetMapping("/page/queryById")
	public ResultVO<PageVO<ProductSpuVO>> pageCategoryProduct(PageCategoryIdRequest pageCategoryIdRequest) {
		PageVO<ProductSpuVO> categoryPageVO = frontCategoryService.pageCategoryProduct(pageCategoryIdRequest);
		return ResultVO.success(categoryPageVO);
	}
}
