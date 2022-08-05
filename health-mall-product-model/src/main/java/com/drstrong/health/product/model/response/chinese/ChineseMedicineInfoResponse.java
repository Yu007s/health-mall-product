package com.drstrong.health.product.model.response.chinese;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/21:26
 */
@Data
@ApiModel("药材编辑页面查询药材返回值")
public class ChineseMedicineInfoResponse implements Serializable {

    private static final long serialVersionUID = 523793164462626498L;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("中药材名字")
    private String medicineName;
}
