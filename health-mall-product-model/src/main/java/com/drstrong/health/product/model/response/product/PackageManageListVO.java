package com.drstrong.health.product.model.response.product;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class PackageManageListVO implements Serializable {

    private static final long serialVersionUID = 1700078824601387245L;

    @ApiModelProperty("套餐ID")
    private Long id;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("套餐编码")
    private String activityPackageCode;

    @ApiModelProperty("套餐商品名称(目前套餐只支持一个商品)")
    private String activitySkuName;

    @ApiModelProperty("套餐商品类型:1-西/成药,3-协定方")
    private Integer productType;

    @ApiModelProperty("店铺 id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("套餐状态(0-待开始，1-进行中, 2-已结束)")
    private Integer activityStatus;

    /**
     * ActivitytatusEnum
     */
    @ApiModelProperty("套餐状态名称")
    private String activityStatusName;

    @ApiModelProperty("价格")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("创建时间")
    private Date createdAt;

    @ApiModelProperty("活动套餐备注")
    private String activityPackageRemark;
}
