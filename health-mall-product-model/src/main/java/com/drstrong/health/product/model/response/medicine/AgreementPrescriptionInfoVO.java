package com.drstrong.health.product.model.response.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@ApiModel("协定方请求实体类")
@Data
public class AgreementPrescriptionInfoVO implements Serializable {

    private static final long serialVersionUID = -8744887762392173646L;

    @ApiModelProperty(value = "协定方id")
    private Long id;

    @ApiModelProperty(value = "协定方名称")
    private String medicineName;

    @ApiModelProperty(value = "协定方编码")
    private String medicineCode;

    @ApiModelProperty("包装规格(0.25g*12片*2板/盒)")
    private String packingSpec;

    @ApiModelProperty("包装单位（盒,瓶...)")
    private String packingUnit;

    @ApiModelProperty("最小包装单位（片,粒...）")
    private String packingUnitLimit;

    @ApiModelProperty("单位包装规格数量（24")
    private Integer packingUnitNumber;

    @ApiModelProperty("拆分单位（板，包，瓶）")
    private String splitUnit;

    @ApiModelProperty("拆分数量")
    private Integer splitValue;

    @ApiModelProperty("处方")
    private String prescriptions;

    @ApiModelProperty("功效")
    private String efficacy;

    @ApiModelProperty("服法")
    private String usageMethod;

    @ApiModelProperty("0：否  1：是")
    private Integer useUsageDosage;

    @ApiModelProperty("图片信息")
    private String imageInfoList;

    @ApiModelProperty("分类信息")
    private String medicineClassificationInfo;

    @ApiModelProperty("用法用量")
    private MedicineUsageVO medicineUsage;
}
