package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.CategoryAttributeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类属性字典表 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:45
 */
@Mapper
public interface CategoryAttributeMapper extends BaseMapper<CategoryAttributeEntity> {
}
