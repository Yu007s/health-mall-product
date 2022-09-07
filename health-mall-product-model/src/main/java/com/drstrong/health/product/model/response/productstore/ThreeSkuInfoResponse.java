package com.drstrong.health.product.model.response.productstore;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc TODO
 * @createTime 2021/12/26 17:25
 * @since TODO
 */
@Data
public class ThreeSkuInfoResponse implements Serializable {
    private static final long serialVersionUID = -5279705434348701633L;

    /**
     * 商城skuID
     */
    private Long skuId;

    /**
     * 三方店铺ID
     */
    private Long storeId;

    /**
     * 三方店铺skuId
     */
    private Long threeSkuId;
}
