package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品 sku库存
 *
 * @author lsx
 * @date 2021/12/6 19:55
 */
@Data
@ApiModel("商品的 sku库存 信息返回值")
public class ProductSkuStockVO implements Serializable {

	private static final long serialVersionUID = 4171276941123718303L;
	@ApiModelProperty("skuId")
	private Long skuId;

	@ApiModelProperty("商品 sku 编码")
	private String skuCode;

	@ApiModelProperty("商品 sku 名称")
	private String skuName;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("商品属性")
	private Integer commAttribute;

	@ApiModelProperty("商品属性名称")
	private String commAttributeName;

	@ApiModelProperty("实物库存")
	private Integer stockNum;
}
