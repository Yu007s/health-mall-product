package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@ApiModel("协定方请求实体类")
@Data
public class AddOrUpdateAgreementRequest implements Serializable {

    private static final long serialVersionUID = 5164966919486101511L;

    @ApiModelProperty(value = "协定方id")
    private Long id;

    @ApiModelProperty(value = "协定方名称")
    @NotEmpty(message = "协定方名称不能为空")
    private String medicineName;

    @ApiModelProperty(value = "协定方编码")
    private String medicineCode;

    @ApiModelProperty("包装规格(0.25g*12片*2板/盒)")
    @NotEmpty(message = "包装规格不能为空")
    private String packingSpec;

    @ApiModelProperty("包装单位（盒,瓶...)")
    @NotEmpty(message = "包装单位不能为空")
    private String packingUnit;

    @ApiModelProperty("最小包装单位（片,粒...）")
    @NotEmpty(message = "最小包装单位不能为空")
    private String packingUnitLimit;

    @ApiModelProperty("单位包装规格数量（24")
    @NotNull(message = "单位包装规格数量不能为空")
    private Integer packingUnitNumber;

    @ApiModelProperty("规格单位（板，包，瓶）")
    private String specUnit;

    @ApiModelProperty("规格值")
    private Integer specValue;

    @ApiModelProperty("处方")
    @NotEmpty(message = "处方")
    private String prescriptions;

    @ApiModelProperty("功效")
    @NotEmpty(message = "功效不能为空")
    private String efficacy;

    @ApiModelProperty("服法")
    @NotEmpty(message = "服法不能为空")
    private String usageMethod;

    @ApiModelProperty("0：否  1：是")
    @NotEmpty(message = "默认用法用量不能为空")
    private Integer useUsageDosage;

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "操作人 姓名", hidden = true)
    private String userName;

    @ApiModelProperty("分类信息")
    @Valid
    private MedicineClassificationInfoRequest classificationInfo;

    @ApiModelProperty("图片信息")
    @NotEmpty(message = "图片不能为空")
    private List<MedicineImageRequest> imageInfoList;

    @ApiModelProperty("用法用量")
    private MedicineUsageRequest medicineUsage;

    @Data
    @ApiModel("分类id")
    public static class MedicineClassificationInfoRequest implements Serializable {

        private static final long serialVersionUID = 2148769498519904439L;

        @ApiModelProperty(value = "剂型分类id")
        @NotNull(message = "剂型分类不能为空")
        private Long agentClassificationId;

        @ApiModelProperty(value = "安全分类id")
        @NotNull(message = "安全分类不能为空")
        private Long securityClassificationId;

        @ApiModelProperty(value = "原料分类id")
        @NotNull(message = "原料分类不能为空")
        private Long materialsClassificationId;
    }
}
