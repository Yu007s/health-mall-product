package com.drstrong.health.product.model.response.cms;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(description = "sku销售城市VO")
public class SkuProhibitAreaVO implements Serializable {

	/**
	 * id
	 */
	private Long id;

	/**
	 * 省 市 区 名称
	 */
	private String name;

	/**
	 * 省市区全名
	 */
	private String fullName;

	/**
	 * 类型:0:全国 1:省 2;市 3:区
	 */
	private Integer type;

	/**
	 * 直辖市 0：不是；1：是
	 */
	private Integer municipality;

}
