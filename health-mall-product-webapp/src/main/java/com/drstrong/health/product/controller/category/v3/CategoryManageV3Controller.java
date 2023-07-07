package com.drstrong.health.product.controller.category.v3;

import com.drstrong.health.product.facade.category.CategoryFacade;
import com.drstrong.health.product.model.request.category.v3.SaveCategoryRequest;
import com.drstrong.health.product.model.request.category.v3.SearchCategoryRequest;
import com.drstrong.health.product.model.response.PageVO;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.v3.CategoryManageRemoteApi;
import com.drstrong.health.product.service.category.v3.OldCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/12 16:43
 */
@RestController
@RequestMapping("/inner/category/manage")
public class CategoryManageV3Controller implements CategoryManageRemoteApi {
	@Resource
	CategoryFacade categoryFacade;

	@Resource
	OldCategoryService oldCategoryService;

	@Override
	public ResultVO<List<CategoryVO>> queryAllCategoryByProductType(Integer productType, Boolean needFilter) {
		return ResultVO.success(categoryFacade.queryAllCategoryByProductType(productType, needFilter));
	}

	@Override
	public ResultVO<Void> saveCategoryEntity(SaveCategoryRequest saveCategoryRequest) {
		oldCategoryService.saveEntity(saveCategoryRequest, saveCategoryRequest.getProductType());
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> deleteCategoryEntity(Long categoryId, String changedName) {
		oldCategoryService.deleteEntity(categoryId, changedName);
		return ResultVO.success();
	}

	@Override
	public ResultVO<PageVO<CategoryVO>> pageSearch(SearchCategoryRequest searchCategoryRequest) {
		PageVO<CategoryVO> categoryPageVo = oldCategoryService.pageSearch(searchCategoryRequest);
		return ResultVO.success(categoryPageVo);
	}

	@Override
	public ResultVO<Void> updateCategoryStatus(Long categoryId, Integer status, String changedName) {
		oldCategoryService.updateCategoryStatus(categoryId, status, changedName);
		return ResultVO.success();
	}
}
