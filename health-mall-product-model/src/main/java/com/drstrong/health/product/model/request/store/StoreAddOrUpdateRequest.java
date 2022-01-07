package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 添加店铺的入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:04
 */
@Data
@ApiModel("添加或更新店铺的入参")
public class StoreAddOrUpdateRequest implements Serializable {
	private static final long serialVersionUID = -2404566779505713412L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	@NotBlank(message = "店铺名称不能为空")
	private String name;
	@NotNull
	@ApiModelProperty("店铺状态")
	private Integer status;
}
