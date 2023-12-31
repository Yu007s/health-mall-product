package com.drstrong.health.product.model.request.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@ApiModel("药品规格")
@Data
public class AddOrUpdateMedicineSpecRequest implements Serializable {

    private static final long serialVersionUID = 1793118656622245772L;

    @ApiModelProperty(value = "规格id")
    private Long id;

    @ApiModelProperty(value = "药品ID")
    private Long medicineId;

    @ApiModelProperty(value = "规格编码")
    private String specCode;

    @ApiModelProperty(value = "规格名称")
    private String specName;

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

    @ApiModelProperty("单位包装规格数量")
    @NotNull(message = "单位包装规格数量不能为空")
    @Min(value = 1, message = "单位包装不能小于1")
    @Max(value = 9999, message = "单位包装不能大于9999")
    private Integer packingUnitNumber;

    @ApiModelProperty("规格单位（板，包，瓶）")
    private String specUnit;

    @ApiModelProperty("规格值")
    @Min(value = 0, message = "规格值数量不能小于0")
    @Max(value = 9999, message = "规格值数量不能大于9999")
    private Integer specValue = 0;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty(value = "操作人 id", hidden = true)
    private Long userId;

    @ApiModelProperty(value = "操作人 姓名", hidden = true)
    private String userName;

    @ApiModelProperty("图片信息")
    private List<MedicineImageRequest> imageInfoList;

    @ApiModelProperty("用法用量")
    private MedicineUsageRequest medicineUsage;

}
