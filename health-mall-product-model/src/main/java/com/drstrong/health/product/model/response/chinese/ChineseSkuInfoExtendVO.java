package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/8/4 10:21
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel("中药sku扩展信息")
public class ChineseSkuInfoExtendVO extends ChineseSkuInfoVO implements Serializable {
	private static final long serialVersionUID = -8483976817745897650L;

	@ApiModelProperty("库存信息")
	private List<StockInfoVO> stockInfoVOList;

	@Data
	@ApiModel("库存信息")
	public static class StockInfoVO implements Serializable {
		private static final long serialVersionUID = 7777626100052725022L;

		@ApiModelProperty("库存数,单位:毫克")
		private BigDecimal virtualQuantity;

		@ApiModelProperty("供应商 id")
		private Long supplierId;

		@ApiModelProperty("供应商名称")
		private String supplierName;
	}
}
