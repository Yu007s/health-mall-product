package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改三方信息 入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 11:23
 */
@Data
@ApiModel("修改三方信息 入参")
public class UpdateThreeRequest implements Serializable {
	private static final long serialVersionUID = -1098992482960555090L;

	@ApiModelProperty("三方 skuId")
	private String threeSkuId;

	@ApiModelProperty("进货单价")
	private BigDecimal purchasePrice;

	@ApiModelProperty("商城的 skuId")
	private Long skuId;
}
