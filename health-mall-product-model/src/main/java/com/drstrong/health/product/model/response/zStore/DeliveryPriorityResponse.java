package com.drstrong.health.product.model.response.zStore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/11:06
 */
@Data
@ApiModel("配送优先级返回 市区对应优先级返回信息")
public class DeliveryPriorityResponse implements Serializable {
    private static final long serialVersionUID = -1568485241564168748L;

    @ApiModelProperty("区域id")
    private Long areaId;

    @ApiModelProperty("区域名称")
    private String areaName;

    @ApiModelProperty("优先级药店供应商id列表  已排好序")
    private List<Long> supplierIds;
}
