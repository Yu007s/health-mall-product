package com.drstrong.health.product.model.request.store;

/**
 * @author lsx
 * @projectName health-mall-product
 * @desc 各地区邮费
 * @createTime 2021/12/13 19:57
 * @since TODO
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("各地区邮费")
public class AreaPostage implements Serializable {

    private static final long serialVersionUID = -6243167594999262146L;
    
    @ApiModelProperty("区域 id")
    private Long areaId;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("邮费")
    private BigDecimal postage;
}
