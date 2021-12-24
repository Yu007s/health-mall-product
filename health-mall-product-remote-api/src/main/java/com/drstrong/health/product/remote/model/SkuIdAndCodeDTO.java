package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * sku 概要信息
 *
 * @author liuqiuyi
 * @date 2021/12/24 20:02
 */
@Data
@ApiModel("sku 的 id 和 code 信息")
public class SkuIdAndCodeDTO implements Serializable {
	private static final long serialVersionUID = -8721777430935933825L;

	/**
	 * skuId
	 */
	@ApiModelProperty("skuId")
	private Long skuId;

	/**
	 * sku 编码
	 */
	@ApiModelProperty("sku编码")
	private String skuCode;
}
