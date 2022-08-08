package com.drstrong.health.product.model.response.chinese;

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
 * 中药 sku 基础信息
 *
 * @author liuqiuyi
 * @date 2022/8/3 16:53
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel("中药sku基础信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChineseSkuInfoVO implements Serializable {
	private static final long serialVersionUID = 250240804033119353L;

	@ApiModelProperty("商品类型。0-商品，1-药品，2-中药")
	private Integer productType;

	@ApiModelProperty("商品类型名称")
	private String productTypeName;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("药材 id")
	private Long medicineId;

	@ApiModelProperty("药材code")
	private String medicineCode;

	@ApiModelProperty("药材名称")
	private String medicineName;

	@ApiModelProperty("最大剂量值")
	private BigDecimal maxDosage;

	@ApiModelProperty("店铺id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("价格/克,单位：分")
	private BigDecimal price;

	@ApiModelProperty("sku状态")
	private Integer skuState;

	@ApiModelProperty("sku状态名称")
	private String skuStateName;
}
