package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
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
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * huangpeng
 * 2023/7/5 18:46
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("活动套餐管理查询的入参")
public class SaveOrUpdateActivityPackageRequest implements Serializable {

    private static final long serialVersionUID = 2844412126524003412L;

    @NotBlank(message = "套餐名称不能为空")
    @ApiModelProperty("套餐名称")
    @Length(max = 50, message = "套餐名称不能超过50字符")
    private String activityPackageName;

    @ApiModelProperty("套餐编码")
    private String activityPackageCode;

    @NotNull(message = "套餐商品类型不能为空")
    @ApiModelProperty("套餐商品类型  1-西成药")
    private Integer productType;

    @NotNull(message = "店铺ID不能为空")
    @ApiModelProperty("店铺ID")
    private Long storeId;

    @NotNull(message = "套餐商品列表不能为空")
    @ApiModelProperty("套餐商品列表")
    private List<ActivityPackageSkuRequest> activityPackageSkuList;

    @NotNull(message = "套餐原价格不能为空")
    @ApiModelProperty("原价格")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    @DecimalMax(value = "9999999", message = "价格不能大于9999999")
    private BigDecimal originalPrice;

    @NotNull(message = "套餐优惠价格不能为空")
    @ApiModelProperty("优惠价格")
    @DecimalMin(value = "0.01", message = "价格不能小于0.01")
    @DecimalMax(value = "9999999", message = "价格不能大于9999999")
    private BigDecimal preferentialPrice;

    @ApiModelProperty("是否展示原价(0不展示,1展示)，默认不展示")
    private Integer originalAmountDisplay;

    @NotNull(message = "活动套餐的图片信息不能为空")
    @ApiModelProperty("活动套餐的图片信息")
    private List<String> activityPackageImageInfo;

    @NotBlank(message = "活动套餐介绍不能为空")
    @ApiModelProperty("活动套餐介绍")
    @Length(max = 2000, message = "活动套餐介绍不能超过2000字符")
    private String activityPackageIntroduce;

    @ApiModelProperty("活动套餐备注")
    @Length(max = 500, message = "活动套餐备注不能超过500字符")
    private String activityPackageRemark;

    @ApiModelProperty(value = "操作人id", hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "操作人名称", hidden = true)
    private String operatorName;

}
