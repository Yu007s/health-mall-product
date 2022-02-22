package com.drstrong.health.product.model.response.category;

import com.drstrong.health.product.model.BaseTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后台分类返回值
 *
 * @author liuqiuyi
 * @date 2021/12/6 11:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("后台分类的返回值")
public class BackCategoryVO extends BaseTree implements Serializable {
	private static final long serialVersionUID = -7005838690717534006L;

	@ApiModelProperty(value = "分类名")
	private String name;

	@ApiModelProperty(value = "节子结点数量")
	private Integer leafCount = 0;

	@ApiModelProperty(value = "路径ID列表，以逗号分隔")
	private String idPath;

	@ApiModelProperty(value = "路径名称列表，以逗号分隔")
	private String namePath;

	@ApiModelProperty(value = "分类级别：以1开始")
	private Integer level = 0;

	@ApiModelProperty(value = "排序号")
	private Integer orderNumber = 0;

	@ApiModelProperty(value = "状态：1-启用，0-禁用, -1-删除")
	private Integer status;

	@ApiModelProperty(value = "已关联商品数量")
	private Integer pNumber = 0;

	@ApiModelProperty(value = "icon图标")
	private String icon;

	@ApiModelProperty(value = "描述")
	private String description;
}
