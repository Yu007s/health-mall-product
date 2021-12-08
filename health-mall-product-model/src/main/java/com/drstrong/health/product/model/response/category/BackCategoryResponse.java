package com.drstrong.health.product.model.response.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台分类返回值
 *
 * @author liuqiuyi
 * @date 2021/12/6 11:31
 */
@Data
@ApiModel("后台分类的返回值")
public class BackCategoryResponse implements Serializable {
	private static final long serialVersionUID = -7005838690717534006L;

	@ApiModelProperty("分类id")
	private Long id;

	@ApiModelProperty("父类 id")
	private Long parentId;

	@ApiModelProperty("分类名称")
	private String category;
}
