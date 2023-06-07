package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.request.OperatorUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liuqiuyi
 * @date 2023/6/5 10:44
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存店铺包邮金额")
public class SaveStorePostageRequest extends OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = -1120369629899354763L;

	@ApiModelProperty("店铺id")
	@NotNull(message = "店铺id不能为空")
	private Long storeId;

	@ApiModelProperty("包邮金额")
	@NotNull(message = "包邮金额不能为空")
	@DecimalMin(value = "0.00", message = "价格不能小于0")
	@DecimalMax(value = "99999.99", message = "价格不能大于99999.99")
	private BigDecimal freePostage;
}
