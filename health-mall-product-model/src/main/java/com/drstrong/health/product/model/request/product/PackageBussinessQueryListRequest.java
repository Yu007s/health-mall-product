package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * huangpeng
 * 2023/7/5 18:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("套餐管理查询的入参")
public class PackageBussinessQueryListRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1842569972108814628L;

    @ApiModelProperty("套餐名称")
    private String activityPackageName;

    @ApiModelProperty("医生所在互联网医院的ID")
    private Integer agencyId;

    @ApiModelProperty("医生所在互联网医院的ID")
    private List<Long> storeIds;

    @ApiModelProperty("套餐状态(0-已下架，1-已上架, 2-预约上架中, 3-预约下架中)")
    private Integer activityStatus;

}
