package com.drstrong.health.product.model.dto.medicine.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 中药材库的方法返回值，之后可以根据情况自行扩展
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("中药材库的方法返回值")
public class ChineseMedicineBaseDTO {
    private static final long serialVersionUID = 4472572978344867507L;

    @ApiModelProperty("中药材id")
    private Long medicineId;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String medicineName;

    @ApiModelProperty("药材别名")
    private String aliNames;

    @ApiModelProperty("最大剂量")
    private BigDecimal maxDosage;
}
