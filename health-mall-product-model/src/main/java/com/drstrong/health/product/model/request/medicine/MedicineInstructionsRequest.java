package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MedicineInstructionsRequest implements Serializable {

    private static final long serialVersionUID = 415726364754739537L;

    @ApiModelProperty(value = "西药id")
    private Long medicineId;

    @ApiModelProperty(value = "成分")
    private String ingredients;

    @ApiModelProperty(value = "性状")
    private String phenotypicTrait;

    @ApiModelProperty(value = "适应症/功能主治")
    @NotEmpty(message = "适应症/功能主治 不能为空")
    private String indications;

    @ApiModelProperty(value = "用法用量")
    @NotEmpty(message = "用法用量不能为空")
    private String usageDosage;

    @ApiModelProperty(value = "不良反应")
    private String adverseEffects;

    @ApiModelProperty(value = "禁忌")
    private String contraindications;

    @ApiModelProperty(value = "注意事项")
    private String mattersNeedingAttention;

    @ApiModelProperty(value = "药品贮藏")
    @NotEmpty(message = "药品贮藏不能为空")
    private String medicineStorage;

    @ApiModelProperty(value = "生产企业")
    @NotEmpty(message = "生产企业不能为空")
    private String productionEnterprise;

    @ApiModelProperty(value = "上市许可持有人")
    private String listingLicensee;

    @ApiModelProperty(value = "有效期")
    @NotEmpty(message = "有效期不能为空")
    private String periodValidity;

    @ApiModelProperty(value = "执行标准")
    @NotEmpty(message = "执行标准不能为空")
    private String executionStandard;
}
