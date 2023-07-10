package com.drstrong.health.product.model.request.sku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/7/7 17:45
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku查询入参")
public class SkuQueryRequest implements Serializable {
    private static final long serialVersionUID = 4831746428720221733L;

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("sku编码,支持模糊查询")
    private String skuCode;

    @ApiModelProperty("sku名称，支持模糊查询")
    private String skuName;

    /**
     * @see com.drstrong.health.product.model.enums.UpOffEnum
     */
    @ApiModelProperty("sku状态")
    private Integer skuStatus;

    @ApiModelProperty("是否需要查询库存")
    private Boolean needQueryInventory;
}
