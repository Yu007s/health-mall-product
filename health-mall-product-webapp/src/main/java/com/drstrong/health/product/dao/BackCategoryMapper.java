package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.BackCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 后台分类 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface BackCategoryMapper extends BaseMapper<BackCategoryEntity> {

}
