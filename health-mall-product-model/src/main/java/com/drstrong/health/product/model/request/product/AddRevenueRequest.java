package com.drstrong.health.product.model.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加税收编码
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:08
 */
@Data
@ApiModel("保存税收编码")
public class AddRevenueRequest implements Serializable {
	private static final long serialVersionUID = 8788344326919174712L;

	@ApiModelProperty(value = "skuId", notes = "和 skuCode 不能同时为空")
	private Long skuId;

	@ApiModelProperty(value = "sku编码", notes = "和 skuId 不能同时为空")
	private String skuCode;

	@ApiModelProperty("税收编码")
	@NotEmpty(message = "revenueCode 不能为空")
	private String revenueCode;

	@ApiModelProperty("税率")
	@NotNull(message = "revenueRate 不能为空")
	private Integer revenueRate;
}
