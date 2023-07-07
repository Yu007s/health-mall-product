package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * huangpeng
 * 2023/7/5 18:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("套餐管理查询的入参")
public class ActivityPackageManageQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1842569972108814648L;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("店铺id")
    private Long storeId;

    /**
     * 套餐上下架状态；0-已下架，1-已上架, 2-预约上架中, 3-预约下架中
     */
    @ApiModelProperty("套餐状态")
    private int activityStatus;

/*    @ApiModelProperty("套餐上架时间")
    private Date activityPackageStartAt;

    @ApiModelProperty("套餐下架时间")
    private Date activityPackageEndAt;*/
}
