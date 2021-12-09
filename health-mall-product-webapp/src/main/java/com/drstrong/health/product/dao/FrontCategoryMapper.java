package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.category.FrontCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 前台分类 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/8 19:37
 */
@Mapper
public interface FrontCategoryMapper extends BaseMapper<FrontCategoryEntity> {
}
