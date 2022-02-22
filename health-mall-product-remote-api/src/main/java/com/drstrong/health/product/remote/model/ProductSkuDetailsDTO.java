package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情信息
 *
 * @author liuqiuyi
 * @date 2021/12/24 11:21
 */
@Data
@ApiModel("商品详情信息")
public class ProductSkuDetailsDTO implements Serializable {
	private static final long serialVersionUID = 171617734250975708L;

	@ApiModelProperty("商品 id")
	private Long productId;

	@ApiModelProperty("sku id")
	private Long skuId;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("商品sku价格")
	private BigDecimal price;

	@ApiModelProperty("规格值")
	private String packValue;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("商品主图片集合")
	private List<String> imageUrlList;

	@ApiModelProperty("商品详情图片集合")
	private List<String> detailUrlList;

	@ApiModelProperty("商品属性集合")
	private List<ProductPropertyDTO> propertyVOList;
}
