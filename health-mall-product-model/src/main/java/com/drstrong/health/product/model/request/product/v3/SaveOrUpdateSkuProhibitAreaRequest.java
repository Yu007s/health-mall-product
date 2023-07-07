package com.drstrong.health.product.model.request.product.v3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/6/1 11:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存或者更新 sku 禁售地区的入参")
public class SaveOrUpdateSkuProhibitAreaRequest implements Serializable {
	private static final long serialVersionUID = 1206326696588779222L;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("禁售区域 id")
	private Set<Long> areaIdList;
}
