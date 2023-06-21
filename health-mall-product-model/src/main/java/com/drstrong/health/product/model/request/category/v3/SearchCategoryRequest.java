package com.drstrong.health.product.model.request.category.v3;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/21 10:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("查询分类的入参 参照之前的老代码")
public class SearchCategoryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 5239584757622443684L;

    @ApiModelProperty("排序方式")
    private String orderBy;

    @ApiModelProperty("排序字段")
    private String orderByField;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Integer status;

    @ApiModelProperty("父类id")
    private Long parentId;
}
