package com.drstrong.health.product.model.dto.medicine;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 药材库的公共返回值
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:16
 */
@Data
public class MedicineWarehouseBaseDTO implements Serializable {
    private static final long serialVersionUID = 2698630207317303454L;

    /**
     * @see ProductTypeEnum
     */
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("商品类型 名称")
    private String productTypeName;
}
