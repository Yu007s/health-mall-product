package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * sku发票相关 信息
 *
 * @author lsx
 * @date 2022/1/4 17:13
 */
@ApiModel(" sku发票相关 信息")
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SkuInvoiceDTO implements Serializable {
	private static final long serialVersionUID = -8543335962967754196L;

	@ApiModelProperty("税率")
	private Integer revenueRate;

	@ApiModelProperty("商品skuId")
	private Long skuId;

	@ApiModelProperty("商品sku编码")
	private String skuCode;

	@ApiModelProperty("税收编码")
	private String revenueCode;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("规格值")
	private String packValue;
}
