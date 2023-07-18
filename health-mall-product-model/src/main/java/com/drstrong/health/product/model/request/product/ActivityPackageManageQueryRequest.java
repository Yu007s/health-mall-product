package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @ApiModelProperty("套餐名称(模糊检索)")
    private String activityPackageName;

    @ApiModelProperty("套餐名称(模糊检索)")
    private String skuName;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("套餐状态(0-待开始，1-进行中, 2-已结束)")
    private Integer activityStatus;
}
