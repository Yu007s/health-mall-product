package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺运费信息
 *
 * @author lsx
 * @date 2021/12/14 17:13
 */
@ApiModel("店铺运费信息")
@Data
public class StorePostageDTO implements Serializable {

	private static final long serialVersionUID = -2657169600908597903L;
	
	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("店铺包邮额度")
	private BigDecimal freePostage;

	@ApiModelProperty("邮费")
	private BigDecimal postage;
}
