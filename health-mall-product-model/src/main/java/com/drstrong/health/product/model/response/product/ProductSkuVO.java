package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 sku
 *
 * @author liuqiuyi
 * @date 2021/12/6 19:55
 */
@Data
@ApiModel("商品的 sku 信息返回值")
public class ProductSkuVO implements Serializable {
	private static final long serialVersionUID = 1230836141635611297L;

	@ApiModelProperty("商品 id")
	private Long productId;

	@ApiModelProperty("skuId")
	private Long skuId;

	@ApiModelProperty("商品 sku 编码")
	private String skuCode;

	@ApiModelProperty("商品主图")
	private String masterImageUrl;

	@ApiModelProperty("商品 sku 名称")
	private String skuName;

	@ApiModelProperty("上架状态(0-未上架,1-已上架)")
	private Integer skuState;

	@ApiModelProperty("上架状态名称(0-未上架,1-已上架)")
	private String skuStateName;

	@ApiModelProperty("税收编码")
	private String revenueCode;

	@ApiModelProperty("税率")
	private Integer revenueRate;

	@ApiModelProperty("规格名称")
	private String packName;

	@ApiModelProperty("规格值")
	private String packValue;

	@ApiModelProperty("价格")
	private BigDecimal price;

	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty("更新时间")
	private LocalDateTime updateTime;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;
}
