package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.dto.area.AreaDTO;
import com.drstrong.health.product.model.dto.category.CategoryDTO;
import com.drstrong.health.product.model.dto.label.LabelDTO;
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
 * @date 2023/6/10 15:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺 sku 的详细值")
public class StoreSkuDetailDTO extends SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = 1956893513730756544L;

	@ApiModelProperty("店铺名称")
	private String storeName;

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

	@ApiModelProperty("分类 id")
	private List<CategoryDTO> categoryList;

	@ApiModelProperty("禁售区域id")
	private List<AreaDTO> prohibitAreaList;
}
