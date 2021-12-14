package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 属性信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 14:05
 */
@Data
@ApiModel("商品属性信息")
public class PropertyInfoResponse implements Serializable {

	private static final long serialVersionUID = -1921127378375511735L;

	@ApiModelProperty("属性 id")
	private Long attributeItemId;

	@ApiModelProperty("属性键")
	private String attributeKey;

	@ApiModelProperty("属性名称")
	private String attributeName;

	@ApiModelProperty("属性值")
	private String attributeValue;

	@ApiModelProperty("属性类型：1-规格属性，2-特有属性")
	private Integer attributeType;

	@ApiModelProperty("表单类型：1-文本，2-单选，3-多选，4-日期")
	private Integer formType;

	@ApiModelProperty("属性值选项列表，以逗号分隔（单选和多选才有）")
	private String attributeOptions;

	@ApiModelProperty("是否必填：0-非必填，1-必填")
	private Integer required = 0;

	@ApiModelProperty("排序号")
	private Integer orderNumber = 0;
}
