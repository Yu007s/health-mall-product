package com.drstrong.health.product.dao.store;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.store.StoreSkuEntity;
import com.drstrong.health.product.model.entity.store.StoreThreeRelevanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 三方药店关联商品mapper
 * @createTime 2021/12/14 21:12
 * @since TODO
 */
@Mapper
public interface StoreThreeRelevanceMapper extends BaseMapper<StoreThreeRelevanceEntity> {
    @Select("SELECT p.id as skuId,p.sku_code,p.sku_name,p.sku_price as price,p.state as skuState,t.three_purchase_price as intoPrice,t.three_sku_id FROM `pms_product_sku` p LEFT JOIN product_three_store_relevance t ON p.id = t.sku_id "
    + "${ew.customSqlSegment}" + "ORDER BY p.id DESC")
    List<StoreSkuEntity> pageSkuList(Page<StoreSkuEntity> page, @Param(Constants.WRAPPER) QueryWrapper<StoreSkuEntity> queryWrapper);

    @Select("SELECT p.id as skuId,p.sku_code,p.sku_name,p.sku_price as price,p.state as skuState,t.three_purchase_price as intoPrice,t.three_sku_id FROM `pms_product_sku` p LEFT JOIN product_three_store_relevance t ON p.id = t.sku_id "
            + "${ew.customSqlSegment}" + "ORDER BY p.id DESC")
    List<StoreSkuEntity> searchSkuList(@Param(Constants.WRAPPER) QueryWrapper<StoreSkuEntity> queryWrapper);
}
