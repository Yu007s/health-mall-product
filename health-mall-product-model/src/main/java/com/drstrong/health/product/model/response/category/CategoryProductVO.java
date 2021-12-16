package com.drstrong.health.product.model.response.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类商品信息
 *
 * @author liuqiuyi
 * @date 2021/12/16 00:38
 */
@Data
@ApiModel("分类商品信息")
public class CategoryProductVO implements Serializable {
	private static final long serialVersionUID = 8259032647230107186L;

	@ApiModelProperty("商品 id")
	private Long productId;

	@ApiModelProperty("商品名称")
	private String productName;

	@ApiModelProperty("商品主图")
	private String masterImageUrl;

	@ApiModelProperty("最低价格")
	private BigDecimal lowPrice;
}
