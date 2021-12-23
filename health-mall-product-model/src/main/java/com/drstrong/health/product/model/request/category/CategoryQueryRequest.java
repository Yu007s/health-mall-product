package com.drstrong.health.product.model.request.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 分类查询的 request
 *
 * @author liuqiuyi
 * @date 2021/12/6 09:44
 */
@Data
@ApiModel("分类查询的入参")
public class CategoryQueryRequest implements Serializable {
	private static final long serialVersionUID = -2799541005138681444L;

	@ApiModelProperty("分类名称")
	private String categoryName;

	@ApiModelProperty("分类状态 0-启用,1-禁用")
	private Integer state;

	@ApiModelProperty("分类级别")
	private Integer level;

	@ApiModelProperty(value = "分类id 集合", hidden = true)
	private Set<Long> categoryIdList;
}
