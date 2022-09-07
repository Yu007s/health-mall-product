package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xieYueFeng
 * @Date 2022/08/05/18:59
 */
@Data
@ApiModel("配送优先级返回 某区域对应优先级返回信息")
public class DeliveryPriRequest implements Serializable {
    private static final long serialVersionUID = -1564854894456645741L;

    @ApiModelProperty("区域id")
    private List<Long> areaId;

    @ApiModelProperty("优先级药店供应商id列表  已排好序")
    private List<Long> supplierIds;
}
