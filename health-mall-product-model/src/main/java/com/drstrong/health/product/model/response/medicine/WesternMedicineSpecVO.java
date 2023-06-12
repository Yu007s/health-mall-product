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
@ApiModel(description = "西药规格")
public class WesternMedicineSpecVO implements Serializable {

    private static final long serialVersionUID = 5658375168703067635L;

    @ApiModelProperty(value = "规格id")
    private Long id;

    @ApiModelProperty(value = "药品ID")
    private Long medicineId;

    @ApiModelProperty(value = "规格编码")
    private String specCode;

    @ApiModelProperty(value = "规格名称")
    private String specName;

    @ApiModelProperty("0：否  1：是")
    private Integer useUsageDosage;
}
