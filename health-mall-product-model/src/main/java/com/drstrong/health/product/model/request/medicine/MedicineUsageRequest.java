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

    @ApiModelProperty("0：否  1：是")
    private Integer useUsageDosage;

    @ApiModelProperty("关联id")
    @NotNull(message = "relationId不能为空")
    private Long relationId;

    @ApiModelProperty("类型 1：西药 2：协定方")
    @NotNull(message = "关联类型 1：西药 2：协定方")
    private Integer relationType;

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

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "操作人 姓名", hidden = true)
    private String userName;

    public void assignmentRelation(Long relationId, Integer relationType) {
        this.relationId = relationId;
        this.relationType = relationType;
    }

}
