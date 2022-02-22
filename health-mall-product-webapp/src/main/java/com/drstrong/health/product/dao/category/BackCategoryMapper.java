package com.drstrong.health.product.dao.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 后台分类 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface BackCategoryMapper extends BaseMapper<BackCategoryEntity> {
	/**
	 * 根据条件查询 后台台分类
	 *
	 * @param categoryQueryRequest 参数
	 * @return 返回值
	 * @author liuqiuyi
	 * @date 2021/12/9 14:25
	 */
	List<BackCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest);

	/**
	 * 单个插入
	 * <p> 仅用于数据同步使用 </>
	 *
	 * @param categoryEntity 保存入参
	 * @author liuqiuyi
	 * @date 2022/1/18 09:53
	 */
	void syncDateInsertOne(BackCategoryEntity categoryEntity);

	/**
	 * 下面的都是之前的方法,未改动,直接拷贝
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 17:57
	 */
	int updateLeafCountByIds(@Param("categoryIds") Collection<Long> categoryIds, @Param("offset") Integer offset);
	int updateStatusById(@Param("categoryId") Long categoryId, @Param("status") Integer status);
	int updatePNumberById(@Param("categoryId") Long categoryId, @Param("offset") Integer offset);
	int updateChildrenNamePath(@Param("idPath") String idPath,
							   @Param("oldNamePath") String oldNamePath, @Param("newNamePath") String newNamePath);
}
