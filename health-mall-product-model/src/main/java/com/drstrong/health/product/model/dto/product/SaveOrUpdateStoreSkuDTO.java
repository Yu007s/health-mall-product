package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.dto.label.SkuIncentivePolicyDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/5/31 17:56
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存店铺的 sku 信息入参")
public class SaveOrUpdateStoreSkuDTO extends SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = 2833312126524003412L;

	@ApiModelProperty("安全用药库的编码")
	private String medicineCode;

	@ApiModelProperty("sku名称")
	private String skuName;

	@ApiModelProperty("销售价格,单位:元")
	private BigDecimal salePrice;

	@ApiModelProperty("供应商信息")
	private List<SupplierInfoDTO> supplierInfoList;

	@ApiModelProperty("标签信息")
	private List<LabelDTO> labelList;

	@ApiModelProperty("sku 的激励政策")
	private List<SkuIncentivePolicyDTO> skuIncentivePolicyList;
}
