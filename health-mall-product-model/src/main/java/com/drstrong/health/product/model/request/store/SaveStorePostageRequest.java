package com.drstrong.health.product.model.request.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiuyi
 * @date 2023/6/5 10:44
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存店铺包邮金额")
public class SaveStorePostageRequest implements Serializable {
	private static final long serialVersionUID = -1120369629899354763L;

	@ApiModelProperty("店铺id")
	private Long storeId;

	@ApiModelProperty("包邮金额")
	private BigDecimal freePostage;
}
