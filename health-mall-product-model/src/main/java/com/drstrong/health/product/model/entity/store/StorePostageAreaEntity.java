package com.drstrong.health.product.model.entity.store;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 店铺地区邮费
 * @createTime 2021/12/13 10:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("product_store_postage_area")
public class StorePostageAreaEntity extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -3065711028386087587L;
    /**
     * 自增主键
     */
    private Long id;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 地区ID
     */
    private Long areaId;

    /**
     * 地区名称
     */
    private String areaName;


    /**
     * 邮费
     */
    private BigDecimal postage;
}
