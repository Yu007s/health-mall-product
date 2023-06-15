package com.drstrong.health.product.model.response.medicine;


import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @since 2023-06-15
 */

@Data
@ApiModel(description = "西药导出excel信息VO")
public class WesternMedicineExcelVO implements Serializable {

    private static final long serialVersionUID = -3280772519726465951L;

    @ApiModelProperty("药品编码")
    private String medicineCode;

    @ApiModelProperty("药品代码")
    private String standardSpecCode;

    @ApiModelProperty("商品名")
    private String medicineName;

    @ApiModelProperty("通用名")
    private String commonName;

    @ApiModelProperty("品牌名")
    private String brandName;

    @ApiModelProperty("规格编码")
    private String specCode;

    @ApiModelProperty("规格名称")
    private String specName;

    @ApiModelProperty("规格")
    private String packingSpec;

    @ApiModelProperty("包装单位")
    private String packingUnit;

    @ApiModelProperty("药品分类")
    private String drugClassName;

    @ApiModelProperty("批准文号/注册码")
    private String approvalNumber;

    @ApiModelProperty("生产企业")
    private String productionEnterprise;

    @ApiModelProperty("上市持有许可人")
    private String listingLicensee;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("建码时间")
    private LocalDateTime createdAt;
}
