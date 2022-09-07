package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.enums.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/10:03
 */
@Data
@ApiModel("店铺查找查询入参")
public class StoreSearchRequest implements Serializable {
    private static final long serialVersionUID = -1543848912571564727L;

    @ApiModelProperty("店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("关联互联网医院的id")
    private Long agencyId;

    @ApiModelProperty("店铺类型名称")
    private String storeTypeName;

}
