package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品详情信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 21:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品详情信息")
public class ProductDetailVO extends SpuBaseInfoVO implements Serializable {
	private static final long serialVersionUID = -8217329668962591111L;

	@ApiModelProperty("价格起始值")
	private BigDecimal lowPrice;

	@ApiModelProperty("价格结束值")
	private BigDecimal highPrice;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("规格值")
	private String packValue;

	@ApiModelProperty("购物车中是否有未付款商品")
	private Boolean hasUnpaidFlag;
}
