package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 关联三方药店 入参
 * @createTime 2021/12/15 19:46
 * @since TODO
 */
@Data
@ApiModel("关联三方药店 入参")
public class RelevanceThreeRequest implements Serializable {
    private static final long serialVersionUID = 3812559158551323287L;

    @ApiModelProperty("三方 skuId")
    @NotNull
    private Long threeSkuId;
    @ApiModelProperty("三方 storeId")
    @NotNull
    private Long storeId;
    @NotNull
    @ApiModelProperty("进货单价")
    private BigDecimal threePurchasePrice;
    @NotNull
    @ApiModelProperty("商城的 skuId")
    private Long skuId;
}
