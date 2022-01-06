package com.drstrong.health.product.dao.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.store.StorePostageAreaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺邮费mapper
 * @createTime 2021/12/13 11:14
 * @since TODO
 */
@Mapper
public interface StorePostageAreaMapper extends BaseMapper<StorePostageAreaEntity> {

    /**
     * 批量插入店铺各地区邮费
     *
     * @param items 保存入参
     * @return 影响行数
     * @author lsx
     * @date 2021/12/13 21:10
     */
    Integer batchInsert(@Param("items") List<StorePostageAreaEntity> items);

}
