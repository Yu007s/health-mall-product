package com.drstrong.health.product.model.response.medicine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MedicineInstructionsVO implements Serializable {

    private static final long serialVersionUID = 7653993630335564206L;
    @ApiModelProperty(value = "西药id")
    private Long medicineId;

    @ApiModelProperty(value = "成分")
    private String ingredients;

    @ApiModelProperty(value = "性状")
    private String phenotypicTrait;

    @ApiModelProperty(value = "适应症/功能主治")
    private String indications;

    @ApiModelProperty(value = "用法用量")
    private String usageDosage;

    @ApiModelProperty(value = "不良反应")
    private String adverseEffects;

    @ApiModelProperty(value = "禁忌")
    private String contraindications;

    @ApiModelProperty(value = "注意事项")
    private String mattersNeedingAttention;

    @ApiModelProperty(value = "药品贮藏")
    private String medicineStorage;

    @ApiModelProperty(value = "生产企业")
    private String productionEnterprise;

    @ApiModelProperty(value = "上市许可持有人")
    private String listingLicensee;

    @ApiModelProperty(value = "有效期")
    private String periodValidity;

    @ApiModelProperty(value = "执行标准")
    private String executionStandard;
}
