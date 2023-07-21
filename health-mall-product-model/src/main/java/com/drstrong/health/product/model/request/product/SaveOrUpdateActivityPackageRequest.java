package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.request.incentive.SaveOrUpdateSkuPolicyRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Size(max = 1, message = "套餐商品列表数量超过限制")
    private List<ActivityPackageSkuRequest> activityPackageSkuList;

    @NotNull(message = "套餐价格不能为空")
    @ApiModelProperty("原价格")
    @DecimalMin(value = "0.01", message = "请填写正确的套餐价")
    @DecimalMax(value = "9999999", message = "请填写正确的套餐价")
    private BigDecimal price;

    @ApiModelProperty("套餐活动开始时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "套餐活动开始时间不能为空")
    private LocalDateTime activityStartTime;

    @ApiModelProperty("套餐活动结束时间")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "套餐活动结束时间必须晚于当前时间")
    @NotNull(message = "套餐活动结束时间不能为空")
    private LocalDateTime activityEndTime;

    @ApiModelProperty("活动套餐备注")
    @Length(max = 500, message = "活动套餐备注不能超过500字符")
    private String activityPackageRemark;

    @ApiModelProperty(value = "操作人id", hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "操作人名称", hidden = true)
    private String operatorName;

}
