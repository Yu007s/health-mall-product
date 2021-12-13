package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import com.drstrong.health.product.model.request.category.CategoryQueryRequest;
import org.apache.ibatis.annotations.Mapper;

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
}
