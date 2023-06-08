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
 * @author liuqiuyi
 * @date 2023/6/8 14:09
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku 激励政策返回值")
public class SkuIncentivePolicyDetailVO implements Serializable {
	private static final long serialVersionUID = 1869041756573234529L;

	@ApiModelProperty("sku编码")
	private String skuCode;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("sku供应商信息")
	private List<SupplierInfoVO> supplierInfoList;

	@ApiModelProperty("sku 标签信息")
	private List<LabelDTO> skuLabelList;

	@ApiModelProperty("售价,单位:元")
	private BigDecimal price;

	@ApiModelProperty("成本价,单位:元")
	private BigDecimal costPrice;

	@ApiModelProperty("sku 激励政策详情")
	private List<SkuIncentivePolicyVO> skuIncentivePolicyList;
}
