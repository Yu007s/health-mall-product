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
 * @date 2023/6/5 10:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存店铺供应链的邮费信息")
public class SaveStoreSupplierPostageRequest implements Serializable {
	private static final long serialVersionUID = 624680882600170986L;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("供应商全局邮费")
	private Long freePostage;

	@ApiModelProperty("店铺供应商各区域的邮费设置")
	private List<StoreSupplierAreaPostageVO> storeSupplierAreaPostageList;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	@ApiModel("店铺供应商区域邮费")
	public static class StoreSupplierAreaPostageVO implements Serializable {
		private static final long serialVersionUID = 3288915241929029217L;

		@ApiModelProperty("区域 id")
		private Long areaId;

		@ApiModelProperty("配送费,单位:元")
		private Long postage;
	}
}
