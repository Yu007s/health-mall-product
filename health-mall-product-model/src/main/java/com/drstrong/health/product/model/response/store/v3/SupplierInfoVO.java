package com.drstrong.health.product.model.response.store.v3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/8 14:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺供应商信息 响应值")
public class SupplierInfoVO implements Serializable {
	private static final long serialVersionUID = -2514845438968454170L;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("供应商名称")
	private String supplierName;
}
