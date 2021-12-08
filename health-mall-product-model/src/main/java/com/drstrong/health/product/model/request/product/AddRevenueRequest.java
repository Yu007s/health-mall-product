package com.drstrong.health.product.model.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("税收编码")
	private String revenueCode;

	@ApiModelProperty("税率")
	private Integer revenueRate;
}
