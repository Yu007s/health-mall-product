package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.response.store.SupplierResponse;
import com.drstrong.health.product.model.response.store.delievy.DeliveryPriResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/01/14:37
 */
@Data
public class SaveDeliveryRequest implements Serializable {

    private static final long serialVersionUID = 158724579447657578L;

    /**
     * 店铺id
     */
    @NotNull
    private Long storeId;

    /**
     * 默认供应商优先级
     */
    @NotNull
    private DeliveryPriRequest defaultDelPriority;

    /**
     * 分市区优先级列表
     */
    private List<DeliveryPriRequest> deliveryPriorities;

}
