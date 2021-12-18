package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku 基础信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 21:09
 */
@Data
@ApiModel("sku 基础信息")
public class SkuBaseInfoVO implements Serializable {
	private static final long serialVersionUID = -5089413355055401869L;

	@ApiModelProperty("sku编码")
	private String skuCode;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("规格值")
	private String packValue;

	@ApiModelProperty("价格")
	private BigDecimal price;

	@ApiModelProperty("sku 库存数")
	private Long inventoryNum;
}
