package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author xieYueFeng
 * @Date 2022/08/03/9:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel("一条中药材信息")
public class ChineseMedicineResponse implements Serializable {
    private static final long serialVersionUID = 1515648173694532564L;

    @ApiModelProperty("中药材id")
    private Long medicineId;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String name;

    @ApiModelProperty("药材别名列表")
    private String aliNames;

    @ApiModelProperty("最大剂量")
    private BigDecimal maxDosage;

}
