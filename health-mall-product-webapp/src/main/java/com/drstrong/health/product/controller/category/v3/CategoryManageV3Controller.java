package com.drstrong.health.product.controller.category.v3;

import com.drstrong.health.product.facade.category.CategoryFacade;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;
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
	public ResultVO<List<CategoryVO>> queryAllCategoryByProductType(Integer productType) {
		return ResultVO.success(categoryFacade.queryAllCategoryByProductType(productType));
	}

	@Override
	public ResultVO<Void> saveCategoryEntity(CategoryEntity entity, Integer productType) {
		oldCategoryService.saveEntity(entity, productType);
		return ResultVO.success();
	}

	@Override
	public ResultVO<Void> deleteCategoryEntity(Long categoryId, String changedName) {
		oldCategoryService.deleteEntity(categoryId, changedName);
		return ResultVO.success();
	}
}
