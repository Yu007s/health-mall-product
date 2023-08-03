package com.drstrong.health.product.model.dto.medicine.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协定方返回值，之后可以根据情况自行扩展
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("协定方返回值")
public class AgreementPrescriptionMedicineBaseDTO {
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
