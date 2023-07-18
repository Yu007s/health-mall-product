package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import com.drstrong.health.product.model.request.product.ActivityPackageSkuRequest;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * huangpeng
 * 2023/7/7 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("活动套餐的详细信息")
public class ActivityPackageDetailDTO implements Serializable {

    private static final long serialVersionUID = 1234893513730756544L;

    @ApiModelProperty("套餐ID")
    private Long id;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("套餐编码")
    private String activityPackageCode;

    @ApiModelProperty("套餐商品类型  1-西成药,3-协定方")
    private Integer productType;

    @ApiModelProperty("店铺id")
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
    private BigDecimal price;

    @ApiModelProperty("活动套餐备注")
    private String activityPackageRemark;

    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("套餐商品列表")
    private List<PackageSkuDetailDTO> activityPackageSkuInfoEntityList;

}
