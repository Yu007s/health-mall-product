package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel("药品规格用法用量")
@Data
public class MedicineUsageRequest implements Serializable {

    private static final long serialVersionUID = -2989018544740817595L;

    @ApiModelProperty("药品规格id")
    @NotNull(message = "规格不能为空")
    private Long specificationsId;

    @ApiModelProperty("类型 1：西药 2：协定方")
    @NotNull(message = "类型不能为空")
    private Integer medicineType = 1;

    @ApiModelProperty("用药频次")
    @NotEmpty(message = "用药频次不能为空")
    private String medicationFrequency;

    @ApiModelProperty("用药数量")
    @NotEmpty(message = "用药数量不能为空")
    private String eachDosageCount;

    @ApiModelProperty("用药数量")
    @NotEmpty(message = "用药数量不能为空")
    private String eachDoseUnit;

    @ApiModelProperty("服用时间")
    @NotEmpty(message = "服用时间不能为空")
    private String usageTime;

    @ApiModelProperty("服用方式")
    @NotEmpty(message = "服用方式不能为空")
    private String usageMethod;

}
