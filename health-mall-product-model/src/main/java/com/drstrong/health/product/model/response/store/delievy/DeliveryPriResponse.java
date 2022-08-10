package com.drstrong.health.product.model.response.store.delievy;

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
@ApiModel("配送优先级返回 某区域对应优先级返回信息")
public class DeliveryPriResponse implements Serializable {
    private static final long serialVersionUID = -1568485241564168748L;

    @ApiModelProperty("区域id")
    private List<String> areaId;

    @ApiModelProperty("优先级药店供应商id列表  已排好序")
    private List<String> supplierIds;
}
