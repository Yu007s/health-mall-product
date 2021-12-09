package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.CategoryRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 前后台分类关联 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface CategoryRelationMapper extends BaseMapper<CategoryRelationEntity> {

}
