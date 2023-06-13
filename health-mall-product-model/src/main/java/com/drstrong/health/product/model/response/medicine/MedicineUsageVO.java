package com.drstrong.health.product.model.response.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel("药品规格用法用量")
@Data
public class MedicineUsageVO implements Serializable {


    private static final long serialVersionUID = -2432092485321099595L;

    @ApiModelProperty("关联id")
    private Long relationId;

    @ApiModelProperty("关联类型 1：西药 2：协定方")
    private Integer relationType;

    @ApiModelProperty("用药频次")
    private String medicationFrequency;

    @ApiModelProperty("每次几片,几毫克,几粒等等:有适量的情况")
    private String eachDosageCount;

    @ApiModelProperty("药品单位")
    private String eachDoseUnit;

    @ApiModelProperty("服用时间")
    private String usageTime;

    @ApiModelProperty("服用方式")
    private String usageMethod;

}
