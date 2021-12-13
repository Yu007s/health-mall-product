package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductBasicsInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品基础信息 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:38
 */
@Mapper
public interface ProductBasicsInfoMapper extends BaseMapper<ProductBasicsInfoEntity> {
}
