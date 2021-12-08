package com.drstrong.health.product.model.request.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 更新购物车参数
 *
 * @author liuqiuyi
 * @date 2021/12/7 14:08
 */
@Data
@ApiModel("更新购物车入参")
public class UpdateCartRequest implements Serializable {
	private static final long serialVersionUID = 6416884317227490943L;

	@ApiModelProperty("购物车 id")
	private Long cartId;

	@ApiModelProperty("sku id")
	private Long skuId;

	@ApiModelProperty("购买的数量")
	private Integer count;
}
