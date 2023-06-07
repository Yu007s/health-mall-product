package com.drstrong.health.product.model.response.medicine;


import com.drstrong.health.product.model.BaseTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 药品基础分类
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */

@Data
@ApiModel(description = "药品基础分类VO")
public class MedicineClassificationVO extends BaseTree implements Serializable {

    private static final long serialVersionUID = -2407986828163563817L;

    @ApiModelProperty(value = "节点 id")
    private Long id;

    @ApiModelProperty(value = "父节点")
    private Long parentId;

    @ApiModelProperty("分类名称")
    private String classificationName;

    @ApiModelProperty("类型：1 药理分类，2型剂分类，3药品分类，4安全分类，5原料分类")
    private Integer classificationType;

    @ApiModelProperty("排序")
    private Integer orderNum;

    @ApiModelProperty("分类编码")
    private String code;
}
