package com.drstrong.health.product.model.request.product.v3;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.drstrong.health.product.model.dto.product.SupplierInfoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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
public class SaveOrUpdateStoreSkuRequest extends SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = 2833312126524003412L;

	@ApiModelProperty("安全用药库的编码")
	@NotBlank(message = "安全用药库的编码不能为空")
	private String medicineCode;

	@ApiModelProperty("sku名称")
	@NotBlank(message = "sku 名称不能为空")
	private String skuName;

	@ApiModelProperty("销售价格,单位:元")
	@NotNull(message = "包邮金额不能为空")
	@DecimalMin(value = "0.00", message = "价格不能小于0")
	@DecimalMax(value = "99999.99", message = "价格不能大于99999.99")
	private BigDecimal salePrice;

	@ApiModelProperty("供应商信息")
	@NotNull(message = "供应商信息不能为空")
	private List<SupplierInfoDTO> supplierInfoList;

	@ApiModelProperty("标签信息")
	private Set<Long> labelIdList;

	@ApiModelProperty("分类 id")
	private Set<Long> categoryIdList;

	@ApiModelProperty("禁售区域id")
	private Set<Long> prohibitAreaIdList;
}
