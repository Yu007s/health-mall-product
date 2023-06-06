package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.request.OperatorUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/5 10:50
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存店铺供应链的邮费信息")
public class SaveStoreSupplierPostageRequest extends OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = 624680882600170986L;

	@ApiModelProperty("店铺 id")
	@NotNull(message = "店铺 id 不能为空")
	private Long storeId;

	@ApiModelProperty("供应商 id")
	@NotNull(message = "供应商 id 不能为空")
	private Long supplierId;

	@ApiModelProperty("供应商全局邮费")
	@NotNull(message = "供应商邮费不能为空")
	private BigDecimal freePostage;

	@ApiModelProperty("店铺供应商各区域的邮费设置")
	@Valid
	@NotNull(message = "区域邮费不能为空")
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
		@NotNull(message = "区域id不能为空")
		private Long areaId;

		@ApiModelProperty("配送费,单位:元")
		@NotNull(message = "配送费不能为空")
		private BigDecimal postage;
	}
}
