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
	private Long propertyId;

	@ApiModelProperty("属性名称")
	private String propertyName;

	@ApiModelProperty("属性值")
	private String propertyValue;
}
