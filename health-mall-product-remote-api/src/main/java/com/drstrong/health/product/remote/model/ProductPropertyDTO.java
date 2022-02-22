package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品属性信息返回值
 *
 * @author liuqiuyi
 * @date 2021/12/17 14:28
 */
@Data
@ApiModel("商品属性信息返回值")
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyDTO implements Serializable {
	private static final long serialVersionUID = -8138427300059920122L;

	@ApiModelProperty("商品属性名称")
	private String propertyName;

	@ApiModelProperty("商品属性值")
	private String propertyValue;
}
