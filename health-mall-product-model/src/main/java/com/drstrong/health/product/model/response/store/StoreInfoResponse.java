package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/9:32
 */
@Data
@ApiModel("店铺信息返回值 店铺列表页")
public class StoreInfoResponse implements Serializable {

    private static final long serialVersionUID = 1518948751242657856L;

    @ApiModelProperty("店铺主键id")
    private Long id;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty("店铺类型")
    private String storeTypeName;

    @ApiModelProperty("关联互联网医院名字")
    private String agencyName;

}
