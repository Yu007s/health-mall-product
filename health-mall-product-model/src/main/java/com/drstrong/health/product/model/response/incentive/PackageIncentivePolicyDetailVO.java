package com.drstrong.health.product.model.response.incentive;

import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.response.store.v3.SupplierInfoVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * huangpeng
 * 2023/7/10 14:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("Package激励政策返回值")
public class PackageIncentivePolicyDetailVO implements Serializable {

	private static final long serialVersionUID = 1869041756571434529L;

	@ApiModelProperty("套餐编码")
	private String activityPackageCode;

	@ApiModelProperty("套餐名称")
	private String activityPackageName;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("售价,单位:元")
	private BigDecimal price;

	@ApiModelProperty("成本价,单位:元")
	private BigDecimal costPrice;

	@ApiModelProperty("利润率 (零售价-成本价）/成本价")
	private BigDecimal profit;

	@ApiModelProperty("激励政策详情")
	private List<SkuIncentivePolicyVO> skuIncentivePolicyList;
}
