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

	@ApiModelProperty("商品skuId")
	private Long skuId;

	@ApiModelProperty("商品id")
	private String productId;

	@ApiModelProperty("商品标题")
	private String productName;

	@ApiModelProperty("规格")
	private String packSpecification;

	@ApiModelProperty("商品价格")
	private BigDecimal productAmount;

	@ApiModelProperty("数量")
	private Integer quantity;

	@ApiModelProperty("商品主图")
	private String productIcon;

	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	@ApiModelProperty(value = "店铺名称")
	private String storeName;

	@ApiModelProperty("商品上下架状态 0-未上架,1-已上架")
	private Integer upOffStatus;
}
