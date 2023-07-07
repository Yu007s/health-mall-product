package com.drstrong.health.product.model.response.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * huangpeng
 * 2023/7/5 17:55
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("活动套餐信息")
public class ActivityPackageInfoVO implements Serializable {

    private static final long serialVersionUID = 1700078824601387245L;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("套餐编码")
    private String activityPackageCode;

    @ApiModelProperty("套餐商品类型  1-西成药")
    private Integer productType;

    @ApiModelProperty("店铺 id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("套餐状态(0-已下架，1-已上架, 2-预约上架中, 3-预约下架中)")
    private Integer activityStatus;

    @ApiModelProperty("原价格")
    private BigDecimal originalPrice;

    @ApiModelProperty("优惠价格")
    private BigDecimal preferentialPrice;

    @ApiModelProperty("是否展示原价(0不展示,1展示)")
    private Integer originalAmountDisplay;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("套餐上架时间")
    private Date activityPackageStartAt;

    @ApiModelProperty("套餐下架时间")
    private Date activityPackageEndAt;
}
