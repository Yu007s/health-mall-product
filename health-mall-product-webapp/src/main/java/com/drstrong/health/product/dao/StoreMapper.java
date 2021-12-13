package com.drstrong.health.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.store.StoreEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺mapper
 * @createTime 2021/12/13 11:14
 * @since TODO
 */
@Mapper
public interface StoreMapper extends BaseMapper<StoreEntity> {
    
}
