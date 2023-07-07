package com.drstrong.health.product.model.request.incentive;

import com.drstrong.health.product.model.request.OperatorUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/7 16:41
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaveOrUpdateSkuPolicyRequest extends OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = -4861164147028281732L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("sku编码")
	@NotBlank(message = "sku编码不能为空")
	private String skuCode;

	@ApiModelProperty("成本价")
	@DecimalMin(value = "0.00", message = "成本价不能小于0")
	@DecimalMax(value = "99999.99", message = "成本价不能大于99999.99")
	private BigDecimal costPrice;

	@ApiModelProperty("政策值")
	@Valid
	@NotNull(message = "政策值不能为空")
	private List<SkuIncentivePolicyRequest> skuIncentivePolicyList;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SkuIncentivePolicyRequest implements Serializable {
		private static final long serialVersionUID = 1223700188365707438L;

		@ApiModelProperty("配置表id,对应pms_incentive_policy_config表主键")
		@NotNull(message = "收益单位id不能为空")
		private Long policyConfigId;

		@ApiModelProperty("政策类型 1-固定奖励(单位:元),2-比例提成,3-无收益")
		@NotNull(message = "政策类型不能为空")
		private Integer policyType;

		@ApiModelProperty("政策值")
		@DecimalMin(value = "0.00", message = "政策值不能小于0")
		@DecimalMax(value = "99999.99", message = "政策值不能大于99999.99")
		private BigDecimal policyValue;
	}
}
