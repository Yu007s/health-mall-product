package com.drstrong.health.product.model.response.incentive;

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
 * @date 2023/6/8 14:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku的政策 响应值")
public class SkuIncentivePolicyVO implements Serializable {
	private static final long serialVersionUID = 1869041756573234529L;

	@ApiModelProperty("配置表id,对应pms_incentive_policy_config表主键")
	private Long policyConfigId;

	@ApiModelProperty("配置表对应的名称")
	private String policyConfigName;

	@ApiModelProperty("政策类型 1-固定奖励(单位:元),2-比例提成,3-无收益")
	private Integer policyType;

	@ApiModelProperty("政策类型名称 1-固定奖励(单位:元),2-比例提成,3-无收益")
	private String policyTypeName;

	@ApiModelProperty("政策值")
	private BigDecimal policyValue;
}
