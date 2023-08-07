package com.drstrong.health.product.model.dto.medicine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("规格图片信息")
public class MedicineImageDTO implements Serializable {

    private static final long serialVersionUID = 7806828271240342787L;

    @ApiModelProperty("文件类型 1：大图 2：缩略图 3:icon")
    private Integer type;

    @ApiModelProperty("'文件路径'")
    private String imagePath;

}
