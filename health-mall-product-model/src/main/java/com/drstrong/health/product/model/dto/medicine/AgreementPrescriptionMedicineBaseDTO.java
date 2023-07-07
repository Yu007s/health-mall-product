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
public class AgreementPrescriptionMedicineBaseDTO extends MedicineWarehouseBaseDTO {
    private static final long serialVersionUID = 4472572978344867507L;

    @ApiModelProperty("协定方id")
    private Long medicineId;

    @ApiModelProperty("协定方编码")
    private String medicineCode;

    @ApiModelProperty("协定方名称")
    private String medicineName;

    @ApiModelProperty("协定方完整名称")
    private String fullName;
}
