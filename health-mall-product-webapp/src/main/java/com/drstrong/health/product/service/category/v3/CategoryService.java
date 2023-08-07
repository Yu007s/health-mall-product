package com.drstrong.health.product.service.category.v3;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.category.v3.CategoryEntity;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/12 15:47
 */
public interface CategoryService extends IService<CategoryEntity> {
	/**
	 * 获取西药的所有分类
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 15:50
	 */
	List<CategoryEntity> queryWesternAll();

	/**
	 * 获取健康用品的所有分类
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 15:51
	 */
	List<CategoryEntity> queryHealthAll();

	/**
	 * 根据 id 查询
	 *
	 * @author liuqiuyi
	 * @date 2023/6/12 16:27
	 */
	List<CategoryEntity> listByIds(List<Long> ids);

    List<CategoryEntity> queryWesternCategory();

	List<CategoryEntity> queryHealthCategory();

}
