package com.drstrong.health.product.dao.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.drstrong.health.product.model.entity.product.ProductExtendEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品扩展信息 mapper
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:41
 */
@Mapper
public interface ProductExtendMapper extends BaseMapper<ProductExtendEntity> {
}
