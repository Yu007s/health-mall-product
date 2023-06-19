package com.drstrong.health.product.model.request.category.v3;

import com.drstrong.health.product.model.request.OperatorUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/19 17:51
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存分类的响应值")
public class SaveCategoryRequest extends OperatorUserInfo implements Serializable {
    private static final long serialVersionUID = -94990268475193568L;

    @ApiModelProperty("分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @ApiModelProperty("排序")
    private Long orderNumber;

    @ApiModelProperty("图标地址")
    private String icon;

    @ApiModelProperty("父类id")
    private Long parentId;

    @ApiModelProperty("id，更新必传")
    private Long id;
}
