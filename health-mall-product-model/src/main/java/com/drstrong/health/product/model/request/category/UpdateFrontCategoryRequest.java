package com.drstrong.health.product.model.request.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 更新前台分类的入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 11:37
 */
@Data
@ApiModel("更新前台分类的入参")
public class UpdateFrontCategoryRequest implements Serializable {
	private static final long serialVersionUID = -6813524291141501091L;

	@ApiModelProperty(value = "分类 id", required = true)
	@NotNull(message = "categoryId 不能为空")
	private Long categoryId;

	@ApiModelProperty(value = "分类名称", required = true)
	@NotEmpty(message = "categoryName 不能为空")
	private String categoryName;

	@ApiModelProperty(value = "排序字段", required = true)
	@NotNull(message = "sort 不能为空")
	private Integer sort;

	@ApiModelProperty(value = "icon 图标地址")
	private String iconUrl;

	@ApiModelProperty(value = "关联的后台分类 id 集合")
	private List<Long> backCategoryIdList;
}
