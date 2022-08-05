package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

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
     * 默认供应商优先级列表 id列表
     */
    private List<Long> defaultDeliveries;

    /**
     * 默认供应商优先级列表 名字列表
     */
    private List<String> supplierNames;

    /**
     * 分区域供应商优先级   显示所有被设置过的区域设置
     */
    private List<AreaDeliveryPriority> deliveryPriorities;

}
