package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺的配送费 response
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:33
 */
@Data
@ApiModel("店铺的配送费信息")
public class StorePostageResponse implements Serializable {
	private static final long serialVersionUID = 6078954806565513548L;

	@ApiModelProperty("店铺包邮额度")
	private BigDecimal freePostage;

	@ApiModelProperty("店铺各地区邮费")
	private List<AreaPostage> areaPostageList;

	@Data
	@ApiModel("各地区邮费")
	private static class AreaPostage implements Serializable{
		private static final long serialVersionUID = 1158013994648456439L;

		@ApiModelProperty("区域 id")
		private Long areaId;

		@ApiModelProperty("区域名称")
		private String areaName;

		@ApiModelProperty("邮费")
		private BigDecimal postage;
	}
}
