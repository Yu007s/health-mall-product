package com.drstrong.health.product.remote.api.category;

import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryIdRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 前台分类管理的 api 接口,目前主要提供给 cms 调用
 *
 * @author liuqiuyi
 * @date 2021/12/20 15:23
 */
@Api("健康商城-前台分类管理远程接口")
@FeignClient(value = "health-mall-product", path = "/inner/product/category")
@Deprecated
public interface CategoryManageFacade {
	@ApiOperation(value = "获取所有前台分类", notes = "前台分类较少,前后端讨论后决定不进行分页查询")
	@PostMapping("/front/query")
	ResultVO<List<FrontCategoryVO>> frontQuery(@RequestBody CategoryQueryRequest categoryQueryRequest);

	@ApiOperation(value = "获取所有后台分类", notes = "后台分类较少,前后端讨论后决定不进行分页查询")
	@PostMapping("/back/query")
	ResultVO<List<BackCategoryVO>> backQuery(@RequestBody CategoryQueryRequest categoryQueryRequest);

	@ApiOperation("添加前台分类")
	@PostMapping("/front/add")
	ResultVO<Object> addFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest addOrUpdateFrontCategoryRequest);

	@ApiOperation("更新前台分类")
	@PostMapping("/front/update")
	ResultVO<Object> updateFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest);

	@ApiOperation("更新分类状态")
	@PostMapping("/front/updateState")
	ResultVO<Object> updateStateFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest);

	@ApiOperation("删除分类信息")
	@PostMapping("/front/delete")
	ResultVO<Object> deleteFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest);
}
