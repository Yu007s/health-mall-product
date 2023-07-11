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

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("医生所在互联网医院的ID")
    private Integer agencyId;

    /**
     * 套餐上下架状态；0-已下架，1-已上架, 2-预约上架中, 3-预约下架中
     */
    @ApiModelProperty("套餐状态")
    private Integer activityStatus;

    @ApiModelProperty("套餐创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

}
