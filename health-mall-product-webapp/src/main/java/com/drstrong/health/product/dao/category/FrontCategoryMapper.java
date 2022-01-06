package com.drstrong.health.product.dao.category;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 前台分类 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface FrontCategoryMapper extends BaseMapper<FrontCategoryEntity> {
	/**
	 * 根据条件查询 前台分类
	 *
	 * @param categoryQueryRequest 参数
	 * @return 返回值
	 * @author liuqiuyi
	 * @date 2021/12/9 14:25
	 */
	List<FrontCategoryEntity> queryByParam(CategoryQueryRequest categoryQueryRequest);


	/**
	 * 根据分类 id 逻辑删除,批量操作
	 *
	 * @param categoryIdList 分类 id
	 * @param userId         用户 id
	 * @author liuqiuyi
	 * @date 2021/12/23 09:36
	 */
	void deleteByIdList(@Param("categoryIdList") Set<Long> categoryIdList, @Param("userId") String userId);

	/**
	 * 根据分类 id 修改状态
	 *
	 * @param categoryIdList 分类 id
	 * @param userId         用户 id
	 * @param state          状态
	 * @author liuqiuyi
	 * @date 2021/12/30 19:50
	 */
	void updateStateByIdList(@Param("categoryIdList") Set<Long> categoryIdList, @Param("state") Integer state, @Param("userId") String userId);
}
