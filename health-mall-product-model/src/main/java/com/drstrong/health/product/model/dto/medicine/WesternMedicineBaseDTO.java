package com.drstrong.health.product.model.dto.medicine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 中药材库的方法返回值，之后可以根据情况自行扩展
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class WesternMedicineBaseDTO extends MedicineWarehouseBaseDTO {
    private static final long serialVersionUID = 4472572978344867507L;

    @ApiModelProperty("西药规格编码，对应表中的spec_code")
    private String medicineCode;

    @ApiModelProperty("西药规格名称，对应表中的spec_name")
    private String medicineName;
}
