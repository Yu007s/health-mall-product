package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * huangpeng
 * 2023/7/18 18:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageInfoVO implements Serializable {

    private static final long serialVersionUID = 8761913589559446114L;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("套餐编码")
    private String activityPackageCode;

    @ApiModelProperty("套餐价格")
    private BigDecimal price;

    @ApiModelProperty("活动套餐备注")
    private String activityPackageRemark;

    @ApiModelProperty("套餐商品编码")
    private String skuCode;

    @ApiModelProperty("活动套餐商品名称")
    private String skuName;

    @ApiModelProperty("套餐商品数量")
    private Integer amount;

    @ApiModelProperty("套餐价格")
    private BigDecimal skuPrice;


}
