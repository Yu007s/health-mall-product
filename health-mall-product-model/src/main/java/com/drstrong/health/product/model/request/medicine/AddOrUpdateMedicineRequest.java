package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder
@ApiModel("保存或更新西药请求对象")
public class AddOrUpdateMedicineRequest implements Serializable {

    private static final long serialVersionUID = 5432826826239051693L;

    @ApiModelProperty(value = "药品id")
    private Long id;

    @ApiModelProperty(value = "商品名(cspu药品名称)")
    private String medicineName;

    @ApiModelProperty(value = "通用名")
    @NotBlank(message = "通用名不能为空")
    @Length(max = 50, message = "通用名最大长度不能超过 50 字符")
    private String commonName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "品牌名称")
    private String fullName;

    @ApiModelProperty(value = "药品编号")
    private String medicineCode;

    @ApiModelProperty(value = "汉语拼音")
    @Length(max = 50, message = "汉语拼音最大长度不能超过 50 字符")
    private String pinyin;

    @ApiModelProperty(value = "英文名")
    @Length(max = 50, message = "英文名最大长度不能超过 50 字符")
    private String englishName;

    @ApiModelProperty(value = "化学名称")
    @Length(max = 50, message = "化学名称最大长度不能超过 50 字符")
    private String chemicalName;

    @ApiModelProperty(value = "药品本位码")
    @Length(max = 50, message = "药品本位码最大长度不能超过 50 字符")
    private String standardCode;

    @ApiModelProperty(value = "资料完整", hidden = true)
    private Integer dataIntegrity;

    @ApiModelProperty(value = "批准文号/注册证号")
    @NotBlank(message = "批准文号/注册证号不能为空")
    @Length(max = 50, message = "批准文号最大长度不能超过 50 字符")
    private String approvalNumber;

    @ApiModelProperty(value = "分类信息")
    @Valid
    private MedicineClassificationInfoRequest medicineClassificationInfo;

    @ApiModelProperty(value = "药品说明")
    @Valid
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
        @NotNull(message = "药品分类不能为空")
        private Long drugClassificationId;

        @ApiModelProperty(value = "药理分类id")
        private Long pharmacologyClassificationId;

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

    public void constructFullName() {
        this.fullName = Stream.of(brandName, medicineName, commonName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }
}
