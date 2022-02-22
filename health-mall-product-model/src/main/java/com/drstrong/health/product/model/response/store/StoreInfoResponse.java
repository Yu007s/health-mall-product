package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺信息返回值
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:00
 */
@Data
@ApiModel("店铺信息返回值")
public class StoreInfoResponse implements Serializable {
	private static final long serialVersionUID = -1822176342723341624L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺编码")
	private String storeCode;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("店铺状态")
	private Integer storeStatus;

	@ApiModelProperty("店铺 sku 商品的数量")
	private Integer skuCount;

	@ApiModelProperty("店铺的包邮额度")
	private BigDecimal freePostage;
}
