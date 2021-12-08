package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 更新店铺配送费入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:40
 */
@Data
@ApiModel("更新店铺配送费入参")
public class UpdatePostageRequest implements Serializable {
	private static final long serialVersionUID = 3765971196854701979L;

	@ApiModelProperty("店铺包邮的额度")
	private BigDecimal freePostage;

	@ApiModelProperty("店铺配送费")
	private List<AreaPostageRequest> areaPostageList;

	@Data
	@ApiModel("店铺配送费入参")
	private static class AreaPostageRequest implements Serializable {
		private static final long serialVersionUID = 2410942109181988968L;

		@ApiModelProperty("区域 id")
		private Long areaId;

		@ApiModelProperty("配送费")
		private BigDecimal postage;
	}
}
