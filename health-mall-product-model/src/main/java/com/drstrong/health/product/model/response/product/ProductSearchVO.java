package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品搜索信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:57
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品搜索结果返回值")
public class ProductSearchVO extends SpuBaseInfoVO implements Serializable {
	private static final long serialVersionUID = -4422986487802214588L;

	@ApiModelProperty("icon 地址")
	private String iconUrl;

	@ApiModelProperty("价格开始")
	private BigDecimal priceStart;

	@ApiModelProperty("价格结束")
	private BigDecimal priceEnd;
}
