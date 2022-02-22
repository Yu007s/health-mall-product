package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 规格信息 response
 *
 * @author liuqiuyi
 * @date 2021/12/6 14:48
 */
@Data
@ApiModel("规格信息 response")
public class PackInfoResponse implements Serializable {
	private static final long serialVersionUID = -4934564468144639418L;

	@ApiModelProperty("规格名")
	private String packName;

	@ApiModelProperty("规格值")
	private String packValue;

	@ApiModelProperty("价格")
	private BigDecimal price;

	@ApiModelProperty("属性 id")
	private Integer commAttributeId;

	@ApiModelProperty("属性名称")
	private String commAttributeName;

	@ApiModelProperty("sku编码")
	private String skuCode;

	@ApiModelProperty("skuId")
	private Long skuId;

	@ApiModelProperty("sku 状态 0-未上架,1-已上架")
	private Integer skuState;
}
