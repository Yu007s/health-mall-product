package com.drstrong.health.product.model.dto;

import lombok.Data;

/**
 * 商品属性 DTO
 *
 * @author liuqiuyi
 * @date 2021/12/22 17:14
 */
@Data
public class CommAttributeDTO {
	/**
	 * 商品属性
	 */
	private Integer commAttribute;

	/**
	 * 商品属性值
	 */
	private String commAttributeName;

	/**
	 * 商品属性值获取
	 */
	private String commAttributeIcon;
}
