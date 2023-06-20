package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MedicineInstructionsRequest implements Serializable {

    private static final long serialVersionUID = 415726364754739537L;

    @ApiModelProperty(value = "西药id")
    private Long medicineId;

    @ApiModelProperty(value = "成分")
    @Length(max = 500, message = "成分最大长度不能超过 500 字符")
    private String ingredients;

    @ApiModelProperty(value = "性状")
    @Length(max = 500, message = "性状最大长度不能超过 500 字符")
    private String phenotypicTrait;

    @ApiModelProperty(value = "适应症/功能主治")
    @NotBlank(message = "适应症/功能主治 不能为空")
    @Length(max = 500, message = "适应症最大长度不能超过 500 字符")
    private String indications;

    @ApiModelProperty(value = "用法用量")
    @NotBlank(message = "用法用量不能为空")
    @Length(max = 500, message = "用法用量最大长度不能超过 500 字符")
    private String usageDosage;

    @ApiModelProperty(value = "不良反应")
    @Length(max = 2000, message = "不良反应最大长度不能超过 2000 字符")
    private String adverseEffects;

    @ApiModelProperty(value = "禁忌")
    @Length(max = 2000, message = "禁忌最大长度不能超过 2000 字符")
    private String contraindications;

    @ApiModelProperty(value = "注意事项")
    @Length(max = 2000, message = "注意事项最大长度不能超过 2000 字符")
    private String mattersNeedingAttention;

    @ApiModelProperty(value = "药品贮藏")
    @NotBlank(message = "药品贮藏不能为空")
    @Length(max = 50, message = "药品贮藏最大长度不能超过 50 字符")
    private String medicineStorage;

    @ApiModelProperty(value = "生产企业")
    @NotBlank(message = "生产企业不能为空")
    @Length(max = 50, message = "生产企业最大长度不能超过 50 字符")
    private String productionEnterprise;

    @ApiModelProperty(value = "上市许可持有人")
    @Length(max = 50, message = "上市许可持有人最大长度不能超过 50 字符")
    private String listingLicensee;

    @ApiModelProperty(value = "有效期")
    @NotBlank(message = "有效期不能为空")
    @Length(max = 50, message = "有效期最大长度不能超过 50 字符")
    private String periodValidity;

    @ApiModelProperty(value = "执行标准")
    @NotBlank(message = "执行标准不能为空")
    @Length(max = 100, message = "执行标准最大长度不能超过 100 字符")
    private String executionStandard;
}
