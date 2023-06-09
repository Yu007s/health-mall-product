package com.drstrong.health.product.model.response.medicine;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * 西药信息VO
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */

@Data
@ApiModel(description = "西药信息VO")
public class WesternMedicineVO implements Serializable {

    @ApiModelProperty(value = "西药id")
    private Long id;

    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    @ApiModelProperty(value = "cspu药品名称")
    private String medicineName;

    @ApiModelProperty(value = "药品分类")
    private String drugClassification;

    @ApiModelProperty(value = "安全分类")
    private String securityClassification;

    @ApiModelProperty(value = "生产企业")
    private String productionEnterprise;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "关联规格")
    private Integer relationSpec;

    @ApiModelProperty(value = "资料完整")
    private Integer dataIntegrity;

    @ApiModelProperty(value = "默认用法用量")
    private Integer defaultUsageDosage;
}
