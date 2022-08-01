package com.drstrong.health.product.model.response.zStore;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/10:34
 */
@Data
@ApiModel("配送优先级返回信息")
public class DeliverPriorityVO implements Serializable {
    private static final long serialVersionUID = 156487442328478L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 分市区优先级列表  第0个为默认优先级
     */
    private List<DeliveryPriorityResponse> deliveryPriorities;

    /**
     * 供应商id与供应商名字对应map
     */
    private Map<Long,String> supplierIdNameMap;
}
