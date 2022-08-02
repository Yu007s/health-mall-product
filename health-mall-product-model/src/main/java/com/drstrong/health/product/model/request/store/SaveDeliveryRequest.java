package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author xieYueFeng
 * @Date 2022/08/01/14:37
 */
public class SaveDeliveryRequest implements Serializable {

    private static final long serialVersionUID = 158724579447657578L;


    static class AreaDeliveryPriority {

        @ApiModelProperty("区域id")
        private Long areaId;

        @ApiModelProperty("优先级药店供应商id列表  已排好序")
        private List<Long> supplierIds;
    }

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 默认供应商优先级
     */
    private List<Long> defaultDeliveries;

    /**
     * 分市区优先级列表  第0个为默认优先级
     */
    private List<AreaDeliveryPriority> deliveryPriorities;

    /**
     * 供应商id与供应商名字对应map
     */
    private Map<Long, String> supplierIdNameMap;

}
