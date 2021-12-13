package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
	 * 根据前台分类 id,更新关联状态
	 *
	 * @param frontCategoryId 前台分类 id
	 * @param userId          用户 id
	 * @param state           状态
	 * @return 操作行数
	 * @author liuqiuyi
	 * @date 2021/12/13 11:24
	 */
	int updateStateByFrontCategoryId(@Param("frontCategoryId") Long frontCategoryId, @Param("state") Integer state, @Param("userId") Long userId);
}
