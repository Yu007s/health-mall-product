package com.drstrong.health.product.mq.model.change;

import com.drstrong.health.common.mq.BaseMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/8/3 10:34
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
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
}
