package com.drstrong.health.product.controller.category;

import com.drstrong.health.product.model.enums.ErrorEnums;
import com.drstrong.health.product.model.request.category.AddOrUpdateFrontCategoryRequest;
import com.drstrong.health.product.model.request.category.CategoryIdRequest;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import com.drstrong.health.product.model.response.category.BackCategoryVO;
import com.drstrong.health.product.model.response.category.FrontCategoryVO;
import com.drstrong.health.product.model.response.result.BusinessException;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.CategoryManageFacade;
import com.drstrong.health.product.service.category.BackCategoryService;
import com.drstrong.health.product.service.category.FrontCategoryService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
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
@RequestMapping("/inner/product/category")
@Slf4j
@Api(tags = {"商品分类"})
@Deprecated
public class CategoryManageController implements CategoryManageFacade {
	@Resource
	FrontCategoryService frontCategoryService;

	@Resource
	BackCategoryService backCategoryService;

	@Override
	public ResultVO<List<FrontCategoryVO>> frontQuery(CategoryQueryRequest categoryQueryRequest) {
//		List<FrontCategoryVO> frontCategoryVoList = frontCategoryService.queryByParamToTree(categoryQueryRequest);
//		return ResultVO.success(frontCategoryVoList);
		return ResultVO.success(Lists.newArrayList());
	}

	@Override
	public ResultVO<List<BackCategoryVO>> backQuery(CategoryQueryRequest categoryQueryRequest) {
//		List<BackCategoryVO> backCategoryVOList = backCategoryService.queryByParamToTree(categoryQueryRequest);
//		return ResultVO.success(backCategoryVOList);
		return ResultVO.success(Lists.newArrayList());
	}

	@Override
	public ResultVO<Object> addFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest addOrUpdateFrontCategoryRequest) {
//		frontCategoryService.add(addOrUpdateFrontCategoryRequest);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateFront(@RequestBody @Valid AddOrUpdateFrontCategoryRequest updateFrontCategoryRequest) {
/*		if (Objects.isNull(updateFrontCategoryRequest.getCategoryId())) {
			throw new BusinessException(ErrorEnums.PARAM_IS_NOT_NULL);
		}
		frontCategoryService.update(updateFrontCategoryRequest);*/
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> updateStateFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {
//		frontCategoryService.updateStateFront(categoryIdRequest.getCategoryId(), categoryIdRequest.getUserId());
		return ResultVO.success();
	}

	@Override
	public ResultVO<Object> deleteFront(@RequestBody @Valid CategoryIdRequest categoryIdRequest) {
//		frontCategoryService.deleteFrontCategoryById(categoryIdRequest.getCategoryId(), categoryIdRequest.getUserId());
		return ResultVO.success();
	}
}
