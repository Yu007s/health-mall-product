package com.drstrong.health.product.service.category;

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
	 * @param frontCategoryId 前台分类 id
	 * @return 分类关联信息
	 * @author liuqiuyi
	 * @date 2021/12/9 16:32
	 */
	List<CategoryRelationEntity> getRelationByFrontCategoryId(Long frontCategoryId);

	/**
	 * 根据前台分类 id 集合获取关联信息
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
	Map<Long, Set<Long>> getFrontAndBackCategoryToMap(Set<Long> frontCategoryIdList);

	/**
	 * 批量保存关联信息
	 *
	 * @param relationEntityList 入参集合
	 * @author liuqiuyi
	 * @date 2021/12/10 17:59
	 */
	void batchSave(List<CategoryRelationEntity> relationEntityList);

	/**
	 * 根据主键 id 集合进行逻辑删除
	 *
	 * @param relationIdList 关联关系的主键 id 集合
	 * @author liuqiuyi
	 * @date 2021/12/13 10:52
	 */
	void deletedByIdList(Set<Long> relationIdList);

	/**
	 * 根据前台分类 id 进行逻辑删除
	 *
	 * @param frontCategoryIdList 前台分类 id
	 * @param userId          用户 id
	 * @author liuqiuyi
	 * @date 2021/12/13 11:10
	 */
	void deletedByFrontCategoryIdList(Set<Long> frontCategoryIdList, String userId);

	/**
	 * 根据前台分类 id 进行逻辑删除
	 *
	 * @param frontCategoryIdList 前台分类 id
	 * @param userId              用户 id
	 * @param state 状态
	 * @author liuqiuyi
	 * @date 2021/12/13 11:10
	 */
	void updateStateByFrontCategoryIdList(Set<Long> frontCategoryIdList, Integer state, String userId);
}
