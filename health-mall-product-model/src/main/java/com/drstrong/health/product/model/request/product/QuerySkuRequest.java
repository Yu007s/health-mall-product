package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 查询 sku 的入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 19:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询 sku 的入参")
public class QuerySkuRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = -4324050401624089885L;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("商品名称")
	private String productName;

	@ApiModelProperty("店铺 id")
	private String storeId;

	@ApiModelProperty("开始时间")
	private LocalDateTime createStart;

	@ApiModelProperty("结束时间")
	private LocalDateTime createEnd;

	@ApiModelProperty("价格开始值")
	private Long priceStart;

	@ApiModelProperty("价格结束值")
	private Long priceEnd;
}
