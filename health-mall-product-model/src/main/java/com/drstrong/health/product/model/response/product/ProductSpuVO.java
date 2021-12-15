package com.drstrong.health.product.model.response.product;

import com.drstrong.health.product.model.entity.product.ProductSkuEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品 spu
 *
 * @author liuqiuyi
 * @date 2021/12/6 19:55
 */
@Data
@ApiModel("商品的 spu 信息返回值")
public class ProductSpuVO implements Serializable {
	private static final long serialVersionUID = -6531793503018149420L;

	@ApiModelProperty("商品 id")
	private Long productId;

	@ApiModelProperty("商品编码")
	private String spuCode;

	@ApiModelProperty("商品名称")
	private String spuName;

	@ApiModelProperty("商品 icon 地址")
	private String spuIcon;

	@ApiModelProperty("商品下 sku 数量")
	private Integer skuCount;

	@ApiModelProperty(value = "商品下 sku 集合")
	private List<ProductSkuEntity> skuList;

	@ApiModelProperty(value = "商品下 sku 集合")
	private BigDecimal highPrice;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

	@ApiModelProperty("更新时间")
	private LocalDateTime updateTime;
}
