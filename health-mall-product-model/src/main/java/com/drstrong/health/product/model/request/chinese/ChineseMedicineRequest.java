package com.drstrong.health.product.model.request.chinese;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/07/30/10:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("安全用药分页查询中药材")
public class ChineseMedicineRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 6346193224178357890L;
    @ApiModelProperty(value = "中药材id",required = false)
    private String medicineCode;

    @ApiModelProperty(value = "中药材名字",required = false)
    private String medicineName;

}
