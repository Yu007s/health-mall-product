package com.drstrong.health.product.model.request.product;

import com.drstrong.health.product.model.enums.UpOffEnum;
import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createStart;

	@ApiModelProperty("结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate createEnd;

	@ApiModelProperty("价格开始值")
	private Long priceStart;

	@ApiModelProperty("价格结束值")
	private Long priceEnd;

	@ApiModelProperty(value = "上下架状态", hidden = true)
	private UpOffEnum upOffEnum;

	@ApiModelProperty(value = "商品 id 集合", hidden = true)
	private Set<Long> productIdList;
}
