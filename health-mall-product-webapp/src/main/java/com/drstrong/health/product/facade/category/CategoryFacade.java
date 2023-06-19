package com.drstrong.health.product.facade.category;

import com.drstrong.health.product.model.response.category.v3.CategoryVO;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:58
 */
public interface CategoryFacade {

	/**
	 * 根据内心获取分类
	 * <p>
	 * 中西药分类的返回值中去除了 协定方
	 * </>
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 16:05
	 */
	List<CategoryVO> queryAllCategoryByProductType(Integer productType, Boolean needFilter);
}
