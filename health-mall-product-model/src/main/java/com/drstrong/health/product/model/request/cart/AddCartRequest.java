package com.drstrong.health.product.model.request.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加购物车的入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 13:58
 */
@Data
@ApiModel("添加购物车")
public class AddCartRequest implements Serializable {
	private static final long serialVersionUID = -1173666893104420122L;

	@ApiModelProperty(value = "业务类型，0-商品,1-处方药,2-健康辅助方等", example = "0")
	private Integer businessType;

	@ApiModelProperty("业务 Id")
	private String businessId;

	@ApiModelProperty("购买的份数")
	private Long count;
}
