package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.CategoryAttributeItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台分类属性 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/13 14:41
 */
@Mapper
public interface CategoryAttributeItemMapper extends BaseMapper<CategoryAttributeItemEntity> {
	/**
	 * 仅供数据同步使用
	 *
	 * @author liuqiuyi
	 * @date 2021/12/27 17:36
	 */
	@Deprecated
	void syncDateInsertOne(CategoryAttributeItemEntity categoryAttributeItemEntity);
}
