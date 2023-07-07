package com.drstrong.health.product.model.request.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/5/30 17:34
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺供应商邮费设置入参")
public class StoreSupplierPostageRequest implements Serializable {
	private static final long serialVersionUID = 2910103110741437499L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("供应商总配送费,单位:元")
	private Long freePostage;

	@ApiModelProperty("店铺供应商区域邮费入参")
	private List<StoreSupplierAreaPostageRequest> storeSupplierAreaPostageList;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商区域邮费的返回值")
	public static class StoreSupplierAreaPostageRequest implements Serializable {
		private static final long serialVersionUID = 3288915241929029217L;

		@ApiModelProperty("区域 id")
		private Long areaId;

		@ApiModelProperty("区域名称")
		private String areaName;

		@ApiModelProperty("配送费,单位:元")
		private Long postage;
	}
}
