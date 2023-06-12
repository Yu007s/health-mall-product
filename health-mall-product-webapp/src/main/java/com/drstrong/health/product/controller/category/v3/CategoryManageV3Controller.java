package com.drstrong.health.product.controller.category.v3;

import com.drstrong.health.product.facade.category.CategoryFacade;
import com.drstrong.health.product.model.response.category.v3.CategoryVO;
import com.drstrong.health.product.model.response.result.ResultVO;
import com.drstrong.health.product.remote.api.category.v3.CategoryManageRemoteApi;
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

	@Override
	public ResultVO<List<CategoryVO>> queryAllCategoryByProductType(Integer productType) {
		return ResultVO.success(categoryFacade.queryAllCategoryByProductType(productType));
	}
}
