package com.drstrong.health.product.model.dto.label;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiuyi
 * @date 2023/6/1 09:51
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku 激励政策的DTO 对象")
public class SkuIncentivePolicyDTO implements Serializable {
	private static final long serialVersionUID = 4858523364540176759L;

	@ApiModelProperty("sku编码")
	private String skuCode;

	@ApiModelProperty("人员类型 1-销售,2-医生,3-互联网医院,4-其它")
	private Integer personnelType;

	@ApiModelProperty("政策类型 1-固定奖励(单位:分),2-比例提成,3-无收益")
	private Integer policyType;

	@ApiModelProperty("政策值")
	private BigDecimal policyValue;
}
