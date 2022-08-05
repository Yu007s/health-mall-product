package com.drstrong.health.product.model.request.productstore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 店铺 id 入参
 *
 * @author lsx
 * @date 2021/12/6 13:49
 */
@Data
@ApiModel("启用、禁用店铺 id 入参")
public class StoreIdRequest implements Serializable {

	private static final long serialVersionUID = 246723772203278894L;

	@ApiModelProperty(value = "店铺 id", required = true)
	@NotNull(message = "店铺 id 不能为空")
	private Long storeId;
	@NotNull(message = "店铺 状态 不能为空")
	@ApiModelProperty("店铺状态")
	private Integer storeStatus;
}
