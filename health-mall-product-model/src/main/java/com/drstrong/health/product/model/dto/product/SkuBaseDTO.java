package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/5/31 17:53
 */
@Data
public class SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = -8519964155411438139L;

	/**
	 * @see ProductTypeEnum
	 */
	@ApiModelProperty("商品类型")
	private Integer productType;

	@ApiModelProperty("sku编码,更新时必传")
	private String skuCode;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty(value = "操作人id", hidden = true)
	private Long operatorId;

	@ApiModelProperty(value = "操作人名称", hidden = true)
	private String operatorName;
}
