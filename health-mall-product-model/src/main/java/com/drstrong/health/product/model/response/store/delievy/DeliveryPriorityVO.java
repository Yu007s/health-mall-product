package com.drstrong.health.product.model.response.store.delievy;

import com.drstrong.health.product.model.response.store.SupplierResponse;
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
public class DeliveryPriorityVO implements Serializable {
    private static final long serialVersionUID = 156487442328478L;

    /**
     * 店铺id
     */
    private String storeId;

    /**
     * 默认供应商优先级
     */
    private List<String> defaultDeliveries;

    /**
     * 分市区优先级列表
     */
    private List<DeliveryPriResponse> deliveryPriorities;

    private List<SupplierResponse> supplierResponses;

}
