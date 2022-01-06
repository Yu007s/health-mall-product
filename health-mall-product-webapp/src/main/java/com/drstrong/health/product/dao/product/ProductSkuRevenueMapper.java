package com.drstrong.health.product.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductSkuRevenueEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * sku 关联的税收编码
 *
 * @author liuqiuyi
 * @date 2021/12/14 14:54
 */
@Mapper
public interface ProductSkuRevenueMapper extends BaseMapper<ProductSkuRevenueEntity> {
}
