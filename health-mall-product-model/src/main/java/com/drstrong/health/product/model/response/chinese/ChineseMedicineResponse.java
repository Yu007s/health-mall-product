package com.drstrong.health.product.model.response.chinese;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/03/9:39
 */
@Data
@ApiModel("中药材信息列表查询返回")
public class ChineseMedicineResponse implements Serializable {
    private static final long serialVersionUID = 1515648173694532564L;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String name;

    @ApiModelProperty("药材别名列表")
    private List<String> aliNames;

    @ApiModelProperty("最大剂量")
    private BigDecimal maxDosage;

}
