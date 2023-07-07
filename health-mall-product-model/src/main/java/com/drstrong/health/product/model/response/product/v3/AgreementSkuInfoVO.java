package com.drstrong.health.product.model.response.product.v3;

import com.drstrong.health.product.model.dto.label.LabelDTO;
import com.drstrong.health.product.model.dto.product.SupplierInfoDTO;
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
 * @date 2023/5/31 17:08
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("协定方列表展示信息")
public class AgreementSkuInfoVO extends ProductManageQueryVO implements Serializable {
	private static final long serialVersionUID = 1799978824601387245L;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("协定方预制编码")
	private String medicineCode;

	@ApiModelProperty("sku名称")
	private String skuName;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("供应商信息")
	private List<SupplierInfoDTO> supplierInfoList;

	@ApiModelProperty("价格")
	private BigDecimal salePrice;

	@ApiModelProperty("标签信息")
	private List<LabelDTO> labelList;

	@ApiModelProperty("sku 状态")
	private Integer skuStatus;

	@ApiModelProperty("sku 状态名称")
	private String skuStatusName;
}
