package com.drstrong.health.product.model.request.product.v3;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/5/31 16:59
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("商品管理查询的入参")
public class ProductManageQueryRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 1842569972108814648L;

	/**
	 * @see ProductTypeEnum
	 */
	@ApiModelProperty("商品类型")
	private Integer productType;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("sku 状态")
	private Integer skuStatus;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("店铺id")
	private Long storeId;
}
