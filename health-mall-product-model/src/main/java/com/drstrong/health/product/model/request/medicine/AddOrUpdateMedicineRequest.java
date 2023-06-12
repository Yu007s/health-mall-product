package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
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

    @ApiModelProperty(value = "品牌名称")
    private String fullName;

    @ApiModelProperty(value = "药品编号")
    private String medicineCode;

    @ApiModelProperty(value = "汉语简拼")
    private String pinyin;

    @ApiModelProperty(value = "英文名")
    private String englishName;

    @ApiModelProperty(value = "化学名称")
    private String chemicalName;

    @ApiModelProperty(value = "药品本位码")
    private String standardCode;

    @ApiModelProperty(value = "资料完整", hidden = true)
    private Integer dataIntegrity;

    @ApiModelProperty(value = "批准文号/注册证号")
    @NotNull(message = "批准文号/注册证号不能为空")
    private String approvalNumber;

    @ApiModelProperty(value = "分类信息")
    private MedicineClassificationInfoRequest medicineClassificationInfo;

    @ApiModelProperty(value = "药品说明")
    private MedicineInstructionsRequest medicineInstructions;

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "操作人 姓名", hidden = true)
    private String userName;

    @Data
    @ApiModel("分类id")
    public static class MedicineClassificationInfoRequest implements Serializable {

        private static final long serialVersionUID = -4277079128548673211L;

        @ApiModelProperty(value = "药品分类id")
        private Long drugClassificationId;

        @ApiModelProperty(value = "药品分类名称")
        private String drugClassName;

        @ApiModelProperty(value = "药理分类id")
        private Long pharmacologyClassificationId;

        @ApiModelProperty(value = "药理分类名称")
        private Long pharmacologyClassName;

        @ApiModelProperty(value = "剂型分类id")
        private Long agentClassificationId;

        @ApiModelProperty(value = "剂型分类名称")
        private Long agentClassName;

        @ApiModelProperty(value = "安全分类id")
        private Long securityClassificationId;

        @ApiModelProperty(value = "安全分类名称")
        private Long securityClassName;

        @ApiModelProperty(value = "原料分类id")
        private Long materialsClassificationId;

        @ApiModelProperty(value = "原料分类名称")
        private Long materialsClassName;
    }

    public void constructFullName() {
        if (brandName != null || medicineName != null || commonName != null) {
            this.fullName = brandName + medicineName + commonName;
        }
    }
}
