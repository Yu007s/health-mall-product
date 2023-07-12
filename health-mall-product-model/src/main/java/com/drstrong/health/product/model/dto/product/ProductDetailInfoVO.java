package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * huangpeng
 * 2023/7/11 09:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailInfoVO implements Serializable {

    private static final long serialVersionUID = 3791913589559446114L;

    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    /**
     * sku名称（商品名 通用名 规格）
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;

    /**
     * sku类型(1-药品,3-协定方)
     */
    @ApiModelProperty(value = "sku类型(1-药品,3-协定方)")
    private Integer skuType;

    /**
     * 药品编码
     */
    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String storeName;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal salePrice;

    /**
     * 西药详情
     */
    @ApiModelProperty(value = "西药详情")
    private ProductWesternDetailVO productWesternDetailVO;

    /**
     * 协定方详情
     */
    @ApiModelProperty(value = "协定方详情")
    private ProductAgreementDetailVO productAgreementDetailVO;

}
