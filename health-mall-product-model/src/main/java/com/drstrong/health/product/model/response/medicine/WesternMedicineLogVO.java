package com.drstrong.health.product.model.response.medicine;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


/**
 * <p>
 * 西药日志信息VO
 * </p>
 *
 * @author zzw
 * @since 2023-06-09
 */

@Data
@Builder
@ApiModel(description = "西药日志信息VO")
public class WesternMedicineLogVO {

    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationTime;

    @ApiModelProperty(value = "操作账号")
    private String operationAccount;

    @ApiModelProperty(value = "操作类型")
    private Integer operationType;

    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    @ApiModelProperty(value = "操作行为：新建药品[药品编码]")
    private String operationBehavior;

}
