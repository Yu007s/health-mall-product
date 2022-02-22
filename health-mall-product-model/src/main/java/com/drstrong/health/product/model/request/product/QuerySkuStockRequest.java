package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询 sku库存 的入参
 *
 * @author lsx
 * @date 2021/12/6 19:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询 sku库存 的入参")
public class QuerySkuStockRequest extends PageRequest implements Serializable {

	private static final long serialVersionUID = 5756850251001448686L;
	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("商品名称")
	private String skuName;

	@ApiModelProperty("店铺 id")
	private String storeId;

	@ApiModelProperty("商品属性")
	private Integer commAttribute;
}
