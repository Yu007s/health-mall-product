package com.drstrong.health.product.model.response.category.v3;

import com.drstrong.health.product.model.BaseTree;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author liuqiuyi
 * @date 2023/6/12 16:02
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("分类信息 响应值")
public class CategoryVO extends BaseTree {
	private static final long serialVersionUID = 572638940147912756L;

	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("分类名")
	private String name;

	@ApiModelProperty("分类级别：以1开始")
	private Integer level = 0;

	@ApiModelProperty("icon图标")
	private String icon;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty(value = "路径名称列表，以逗号分隔")
	private String namePath;

	@ApiModelProperty(value = "状态：1-启用，0-禁用, -1-删除")
	private Integer status;

	@ApiModelProperty(value = "节子结点数量")
	private Integer leafCount = 0;

	@ApiModelProperty("路径ID列表，以逗号分隔")
	private String idPath;
}
