package com.drstrong.health.product.model.dto.sku;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * huangpeng
 * 2023/7/13 10:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuBusinessListDTO extends SkuBaseDTO implements Serializable {

    private static final long serialVersionUID = 8986893513730756544L;

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("sku编码,更新时必传")
    private String skuCode;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("店铺 id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("销售价格,单位:元")
    private BigDecimal salePrice;

}
