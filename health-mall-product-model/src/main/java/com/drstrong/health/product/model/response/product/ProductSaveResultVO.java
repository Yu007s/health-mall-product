package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 保存商品的返回结果
 *
 * @author liuqiuyi
 * @date 2021/12/18 17:15
 */
@Data
@ApiModel("保存商品接口的返回结果")
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveResultVO implements Serializable {
	private static final long serialVersionUID = -6577068524561702327L;

	@ApiModelProperty("商品 id")
	private Long productId;
}
