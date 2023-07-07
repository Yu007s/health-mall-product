package com.drstrong.health.product.model.response.medicine;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 药品基础分类
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */

@Data
@Builder
@ApiModel(description = "分类VO")
public class FixedClassificationVO implements Serializable {

    private static final long serialVersionUID = -3062507184088112239L;

    @ApiModelProperty("分类名称")
    private String classificationName;

    @ApiModelProperty("类型：1 药理分类，2型剂分类，3药品分类，4安全分类，5原料分类")
    private Integer classificationType;

    @ApiModelProperty("分类信息")
    private List<MedicineClassificationVO> medicineClassificationList;
}
