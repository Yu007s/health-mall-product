package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.dto.medicine.MedicineImageDTO;
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
public class ProductListInfoVO implements Serializable {

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
     * sku类型(0-商品，1-药品，2-中药,3-协定方)
     */
    @ApiModelProperty(value = "sku类型")
    private Integer skuType;

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
     * 商品封面图片
     */
    @ApiModelProperty(value = "商品封面图片")
    private List<MedicineImageDTO> imageInfo;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价")
    private BigDecimal salePrice;

    /**
     * 常用药标识：0，否；1，是
     */
    @ApiModelProperty(value = "常用药标识：0，否；1，是")
    private Integer boxTag;

}
