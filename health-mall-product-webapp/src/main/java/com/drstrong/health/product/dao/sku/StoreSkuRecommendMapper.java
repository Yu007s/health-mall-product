package com.drstrong.health.product.dao.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drstrong.health.product.model.entity.sku.StoreSkuRecommendEntity;
import com.drstrong.health.product.model.request.sku.recommend.PageSkuRecommendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:42
 */
@Mapper
public interface StoreSkuRecommendMapper extends BaseMapper<StoreSkuRecommendEntity> {

    Page<StoreSkuRecommendEntity> pageQueryByParam(Page<StoreSkuRecommendEntity> entityPage, @Param("queryParam") PageSkuRecommendRequest pageSkuRecommendRequest);

    List<StoreSkuRecommendEntity> listQueryByParam(@Param("queryParam") PageSkuRecommendRequest pageSkuRecommendRequest);
}
