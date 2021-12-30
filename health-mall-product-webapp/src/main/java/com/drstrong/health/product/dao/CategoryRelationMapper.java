package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 前后台分类关联 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface CategoryRelationMapper extends BaseMapper<CategoryRelationEntity> {

	/**
	 * 批量插入前后台分类关系
	 *
	 * @param saveParamList 保存入参
	 * @return 影响行数
	 * @author liuqiuyi
	 * @date 2021/12/13 09:50
	 */
	Integer batchInsert(@Param("saveParamList") List<CategoryRelationEntity> saveParamList);

	/**
	 * 根据前台分类 id,删除
	 *
	 * @author liuqiuyi
	 * @date 2021/12/30 19:59
	 */
	void deletedByFrontCategoryIdList(@Param("frontCategoryIdList") Set<Long> frontCategoryIdList, @Param("userId") String userId);

	/**
	 * 根据前台分类 id,更新状态
	 *
	 * @author liuqiuyi
	 * @date 2021/12/30 20:13
	 */
	void updateStateByFrontCategoryIdList(@Param("frontCategoryIdList") Set<Long> frontCategoryIdList, @Param("state") Integer state, @Param("userId") String userId);
}
