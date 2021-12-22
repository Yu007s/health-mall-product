package com.drstrong.health.product.model.response.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品搜索的返回值
 *
 * @author liuqiuyi
 * @date 2021/12/18 14:41
 */
@Data
@ApiModel("商品搜索的返回值")
public class ProductSearchDetailVO implements Serializable {

	private static final long serialVersionUID = -7911068867335421105L;

	@ApiModelProperty("spu 编码")
	private String spuCode;

	@ApiModelProperty("主图地址")
	private String masterImageUrl;

	@ApiModelProperty("商品标题")
	private String productName;

	@ApiModelProperty("价格,这里展示的是最低价格")
	private BigDecimal lowPrice;

	@ApiModelProperty("是否有库存")
	private Boolean hasInventory;
}
