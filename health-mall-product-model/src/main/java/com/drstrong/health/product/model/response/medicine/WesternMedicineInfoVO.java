package com.drstrong.health.product.model.response.medicine;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 西药详情
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */

@Data
@ApiModel(description = "西药详情VO")
public class WesternMedicineInfoVO implements Serializable {

    private static final long serialVersionUID = 5862492838145945034L;

    @ApiModelProperty(value = "药品id")
    private Long id;

    @ApiModelProperty(value = "cspu药品名称")
    private String medicineName;

    @ApiModelProperty(value = "通用名")
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

    @ApiModelProperty(value = "资料完整")
    private Integer dataIntegrity;

    @ApiModelProperty(value = "批准文号/注册证号")
    @NotNull(message = "批准文号/注册证号不能为空")
    private String approvalNumber;

    @ApiModelProperty(value = "分类信息")
    private String medicineClassificationInfo;

    @ApiModelProperty(value = "药品说明")
    private MedicineInstructionsVO medicineInstructions;

}
