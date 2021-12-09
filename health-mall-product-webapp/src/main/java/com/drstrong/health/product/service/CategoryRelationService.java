package com.drstrong.health.product.service;

import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 前后台关联的 service
 *
 * @author liuqiuyi
 * @date 2021/12/9 16:08
 */
public interface CategoryRelationService {

	/**
	 * 根据前台分类 id 获取关联信息
	 *
	 * @param frontCategoryIdList 前台分类 id 集合
	 * @return 分类关联信息
	 * @author liuqiuyi
	 * @date 2021/12/9 16:32
	 */
	List<CategoryRelationEntity> getRelationByFrontCategoryIds(Set<Long> frontCategoryIdList);

	/**
	 * 根据前台分类 id 获取关联信息,并且组装为 map
	 * <p>注意:前台分类能对应多个后台分类</>
	 *
	 * @param frontCategoryIdList 前台分类 id 集合
	 * @return 分类的对应关系, key = 前台分类 id,value=后台分类id集合
	 * @author liuqiuyi
	 * @date 2021/12/9 16:36
	 */
	Map<Long, List<Long>> getFrontAndBackCategoryToMap(Set<Long> frontCategoryIdList);
}
