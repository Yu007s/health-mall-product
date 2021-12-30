package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品 sku 信息
 *
 * @author liuqiuyi
 * @date 2021/12/14 17:13
 */
@ApiModel("商品 sku 信息")
@Data
public class ProductSkuInfoDTO implements Serializable {
	private static final long serialVersionUID = 814358534732690036L;

	@ApiModelProperty("商品id")
	private Long productId;

	@ApiModelProperty("商品编码")
	private String spuCode;

	@ApiModelProperty("商品主图")
	private String masterImageUrl;

	@ApiModelProperty("商品skuId")
	private Long skuId;

	@ApiModelProperty("商品sku编码")
	private String skuCode;

	@ApiModelProperty("商品标题")
	private String productName;

	@ApiModelProperty("商品规格值")
	private String packValue;

	@ApiModelProperty("商品sku价格")
	private BigDecimal productAmount;

	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	@ApiModelProperty(value = "店铺名称")
	private String storeName;

	@ApiModelProperty("商品 sku 上下架状态 0-未上架,1-已上架")
	private Integer upOffStatus;

	@ApiModelProperty("库存数量")
	private Integer stockNum;

	@ApiModelProperty("商品属性")
	private Integer commAttribute;

	@ApiModelProperty("商品属性值")
	private String commAttributeName;

	@ApiModelProperty("商品属性icon")
	private String commAttributeIcon;
}
