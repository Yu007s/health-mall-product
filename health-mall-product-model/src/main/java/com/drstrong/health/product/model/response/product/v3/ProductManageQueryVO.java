package com.drstrong.health.product.model.response.product.v3;

import com.drstrong.health.product.model.enums.ProductTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/5/31 17:07
 */
@Data
public class ProductManageQueryVO implements Serializable {
	private static final long serialVersionUID = 4015717530606980884L;

	/**
	 * @see ProductTypeEnum
	 */
	@ApiModelProperty("商品类型")
	private Integer productType;

	@ApiModelProperty("商品类型名称")
	private String productTypeName;
}
