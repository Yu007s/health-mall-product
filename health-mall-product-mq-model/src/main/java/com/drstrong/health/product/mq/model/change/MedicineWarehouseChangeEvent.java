package com.drstrong.health.product.mq.model.change;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/8/3 10:34
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MedicineWarehouseChangeEvent extends BaseMessage implements Serializable {
    private static final long serialVersionUID = -4601536444866462028L;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * 药材编码
     */
    private String medicineCode;

    /**
     * 药材名称
     */
    private String medicineName;
}
