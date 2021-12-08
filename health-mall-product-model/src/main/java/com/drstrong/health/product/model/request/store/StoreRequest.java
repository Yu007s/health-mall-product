package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 店铺查询入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 09:58
 */
@Data
@ApiModel("店铺信息查询入参")
public class StoreRequest implements Serializable {
	private static final long serialVersionUID = -4311276505950484007L;

	@ApiModelProperty("店铺id")
	private Long storeId;

	@ApiModelProperty("店铺号")
	private String storeCode;

	@ApiModelProperty("店铺名称")
	private String storeName;
}
