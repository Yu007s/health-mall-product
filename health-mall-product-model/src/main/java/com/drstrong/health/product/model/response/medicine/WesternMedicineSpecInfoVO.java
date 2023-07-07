package com.drstrong.health.product.model.response.medicine;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 西药规格详情
 * </p>
 *
 * @author zzw
 * @since 2023-06-10
 */

@Data
@ApiModel(description = "西药规格详情")
public class WesternMedicineSpecInfoVO implements Serializable {


    private static final long serialVersionUID = -5476273500187911526L;

    @ApiModelProperty(value = "规格id")
    private Long id;

    @ApiModelProperty(value = "药品ID")
    private Long medicineId;

    @ApiModelProperty(value = "规格编码")
    private String specCode;

    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    @ApiModelProperty(value = "国家标准药品编码")
    private String standardSpecCode;

    @ApiModelProperty("0：否  1：是")
    private Integer useUsageDosage;

    @ApiModelProperty("包装规格(0.25g*12片*2板/盒)")
    private String packingSpec;

    @ApiModelProperty("包装单位（盒,瓶...)")
    private String packingUnit;

    @ApiModelProperty("最小包装单位（片,粒...）")
    private String packingUnitLimit;

    @ApiModelProperty("单位包装规格数量（24")
    @NotNull(message = "单位包装规格数量不能为空")
    private Integer packingUnitNumber;

    @ApiModelProperty("规格单位（板，包，瓶）")
    @NotEmpty(message = "规格单位不能为空")
    private String specUnit;

    @ApiModelProperty("规格值")
    private Integer specValue;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("规格图片信息")
    private String specImageInfo;

    @ApiModelProperty("药品规格用法用量")
    private MedicineUsageVO medicineUsage;

}
