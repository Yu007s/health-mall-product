package com.drstrong.health.product.model.request.chinese;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/09/11:43
 */
@Data
public class ChineseMedicineSearchRequest implements Serializable {
    private final static long serialVersionUID = 1231894214894213213L;

    @ApiModelProperty("中药材名称")
    private String medicineName;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("第几页")
    private Integer pageNo;

    @ApiModelProperty("页条数")
    private Integer pageSize;

    @ApiModelProperty("剂型")
    private Integer dosageForm;
}
