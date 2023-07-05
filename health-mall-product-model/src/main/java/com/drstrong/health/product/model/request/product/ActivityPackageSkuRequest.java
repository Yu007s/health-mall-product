package com.drstrong.health.product.model.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * huangpeng
 * 2023/7/5 19:08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("活动套餐内商品管理查询的入参")
public class ActivityPackageSkuRequest implements Serializable {

    private static final long serialVersionUID = 2844412126524993412L;

    @ApiModelProperty("sku编码")
    @NotBlank(message = "sku编码不能为空")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @NotBlank(message = "sku 名称不能为空")
    @Length(max = 50, message = "sku名称不能超过50字符")
    private String skuName;

    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("店铺 id")
    private Long storeId;

    @ApiModelProperty("原价")
    @DecimalMin(value = "0.00", message = "原价不能小于0")
    @DecimalMax(value = "99999.99", message = "原价不能大于99999.99")
    private BigDecimal originalPrice;

    @ApiModelProperty("优惠价")
    @DecimalMin(value = "0.00", message = "优惠价不能小于0")
    @DecimalMax(value = "99999.99", message = "优惠价不能大于99999.99")
    private BigDecimal preferential_price;

    @ApiModelProperty("数量")
    @NotBlank(message = "数量不能为空")
    private Integer amount;

}
