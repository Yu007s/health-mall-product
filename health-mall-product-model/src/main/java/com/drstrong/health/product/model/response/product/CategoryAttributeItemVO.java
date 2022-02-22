package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author liuqiuyi
 * @date 2021/12/13 14:33
 */
@Data
@ApiModel(description = "分类属性项VO")
public class CategoryAttributeItemVO implements Serializable {
	private static final long serialVersionUID = 5970703949167591359L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "属性键")
	private String attributeKey;

	@ApiModelProperty(value = "属性名称")
	private String attributeName;

	@ApiModelProperty(value = "属性类型：1-规格属性，2-特有属性")
	private Integer attributeType;

	@ApiModelProperty(value = "表单类型：1-文本，2-单选，3-多选，4-日期")
	private Integer formType;

	@ApiModelProperty(value = "属性值选项列表，以逗号分隔（单选和多选才有）")
	private String attributeOptions;

	@ApiModelProperty("分类ID")
	private Long categoryId;

	@ApiModelProperty("分类属性ID")
	private Long attributeId;

	@ApiModelProperty(value = "是否必填：0-非必填，1-必填")
	private Integer required = 0;

	@ApiModelProperty(value = "排序号")
	private Integer orderNumber = 0;

	@ApiModelProperty(value = "属性值选项列表，以逗号分隔（单选和多选才有）")
	private List<Map<String, Object>> options;
}
