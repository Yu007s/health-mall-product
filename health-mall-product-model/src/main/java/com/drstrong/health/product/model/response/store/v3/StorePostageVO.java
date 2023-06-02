package com.drstrong.health.product.model.response.store.v3;

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
 * @date 2023/5/30 16:57
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺邮费的返回值")
public class StorePostageVO implements Serializable {
	private static final long serialVersionUID = 5964939554214637042L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("店铺总邮费")
	private Long freePostage;

	@ApiModelProperty("店铺供应商邮费信息")
	private List<StoreSupplierPostageVO> storeSupplierPostageList;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商邮费的返回值")
	public static class StoreSupplierPostageVO implements Serializable {
		private static final long serialVersionUID = 4650535163506324913L;

		@ApiModelProperty("供应商 id")
		private Long supplierId;

		@ApiModelProperty("供应商名称")
		private String supplierName;

		@ApiModelProperty("供应商类型")
		private Integer supplierType;

		@ApiModelProperty("供应商类型名称")
		private String supplierTypeName;

		@ApiModelProperty("供应商全局邮费")
		private Long freePostage;

		@ApiModelProperty("店铺供应商各区域的邮费设置")
		private List<StoreSupplierAreaPostageVO> storeSupplierAreaPostageList;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商区域邮费的返回值")
	public static class StoreSupplierAreaPostageVO implements Serializable {
		private static final long serialVersionUID = 3288915241929029217L;

		@ApiModelProperty("区域 id")
		private Long areaId;

		@ApiModelProperty("区域名称")
		private String areaName;

		@ApiModelProperty("配送费,单位:元")
		private Long postage;
	}
}
