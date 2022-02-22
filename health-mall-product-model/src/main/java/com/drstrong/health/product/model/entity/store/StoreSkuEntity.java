package com.drstrong.health.product.model.entity.store;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc TODO
 * @createTime 2021/12/28 19:47
 * @since TODO
 */
@Data
public class StoreSkuEntity implements Serializable {

    private static final long serialVersionUID = -4026478780908866897L;
    
    private String skuId;

    private String skuCode;

    private String skuName;

    private Integer intoPrice;

    private Integer price;

    private Long threeSkuId;

    private Integer skuState;
}
