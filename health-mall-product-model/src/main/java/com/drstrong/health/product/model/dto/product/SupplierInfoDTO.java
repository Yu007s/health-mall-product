package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiuyi
 * @date 2023/5/31 17:15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺供应商信息")
public class SupplierInfoDTO implements Serializable {
	private static final long serialVersionUID = -7912596287585562333L;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("供应商名称")
	private String supplierName;

	@ApiModelProperty("库存类型。0-实物库存，1-无限库存，2-虚拟库存")
	@Max(value = 2, message = "库存类型不正确")
	@NotNull(message = "库存类型不能为空")
	private Integer stockType;

	@ApiModelProperty("虚拟库存数")
	private BigDecimal virtualQuantity;
}