package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/05/9:46
 */
@Data
@ApiModel("店铺信息id 名称返回值")
public class StoreIdNameResponse implements Serializable {
    private static final long serialVersionUID = -5456489446173665786L;

    @ApiModelProperty("店铺主键id")
    private String id;

    @ApiModelProperty("店铺名称")
    private String storeName;
}
