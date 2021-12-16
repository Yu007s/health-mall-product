package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺 sku 返回值
 * @author liuqiuyi
 * @date 2021/12/7 11:02
 */
@Data
@ApiModel("店铺的 sku 返回值")
public class StoreSkuResponse implements Serializable {
	private static final long serialVersionUID = -4068248972006761569L;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("进货价")
	private BigDecimal intoPrice;

	@ApiModelProperty("销售价格")
	private BigDecimal price;

	@ApiModelProperty("三方 skuId")
	private Long threeSkuId;

	@ApiModelProperty("sku 状态")
	private Integer skuState;
}
