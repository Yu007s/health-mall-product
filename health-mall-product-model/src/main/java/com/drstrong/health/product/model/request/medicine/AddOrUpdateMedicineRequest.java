package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel("保存或更新西药请求对象")
public class AddOrUpdateMedicineRequest implements Serializable {

    private static final long serialVersionUID = 5432826826239051693L;

    @ApiModelProperty(value = "药品id")
    private Long id;

    @ApiModelProperty(value = "cspu药品名称")
    @NotNull(message = "药品名称不能为空")
    private String medicineName;

    @ApiModelProperty(value = "通用名")
    @NotNull(message = "通用名不能为空")
    private String commonName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "药品编号")
    private String medicineCode;

    @ApiModelProperty(value = "汉语简拼")
    private String pinyin;

    @ApiModelProperty(value = "英文名")
    private String englishName;

    @ApiModelProperty(value = "化学名称")
    private String chemicalName;

    /**
     * 分类信息，json
     */
    private String medicineCategoryInfo;

    @ApiModelProperty(value = "药品本位码")
    private String standardCode;

    @ApiModelProperty(value = "资料完整")
    private Integer dataIntegrity;

    @ApiModelProperty(value = "批准文号/注册证号")
    @NotNull(message = "批准文号/注册证号不能为空")
    private String approvalNumber;

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private String userId;


    @Data
    @ApiModel("药品说明")
    public static class MedicineInstructionsRequest implements Serializable {

        private static final long serialVersionUID = 732108391993017126L;

        @ApiModelProperty(value = "西药id")
        private Long medicineId;

        @ApiModelProperty(value = "成分")
        private String ingredients;

        @ApiModelProperty(value = "性状")
        private String phenotypicTrait;

        @ApiModelProperty(value = "适应症/功能主治")
        @NotNull(message = "适应症/功能主治 不能为空")
        private String indications;

        @ApiModelProperty(value = "用法用量")
        @NotNull(message = "用法用量不能为空")
        private String usageDosage;

        @ApiModelProperty(value = "不良反应")
        private String adverseEffects;

        @ApiModelProperty(value = "禁忌")
        private String contraindications;

        @ApiModelProperty(value = "注意事项")
        private String mattersNeedingAttention;

        @ApiModelProperty(value = "药品贮藏")
        @NotNull(message = "药品贮藏不能为空")
        private String medicineStorage;

        @ApiModelProperty(value = "生产企业")
        @NotNull(message = "生产企业不能为空")
        private String productionEnterprise;

        @ApiModelProperty(value = "上市许可持有人")
        private String listingLicensee;

        @ApiModelProperty(value = "有效期")
        @NotNull(message = "有效期不能为空")
        private String periodValidity;

        @ApiModelProperty(value = "执行标准")
        @NotNull(message = "执行标准不能为空")
        private String executionStandard;
    }
}
