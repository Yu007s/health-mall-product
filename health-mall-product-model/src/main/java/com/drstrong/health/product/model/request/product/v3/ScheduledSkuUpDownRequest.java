package com.drstrong.health.product.model.request.product.v3;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liuqiuyi
 * @date 2023/6/1 14:33
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("定时上下架")
public class ScheduledSkuUpDownRequest extends SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = 7661162160501973684L;

	@ApiModelProperty("0-定时下架,1-定时上架")
	private Integer scheduledType;

	@ApiModelProperty("时间")
	private LocalDateTime localDateTime;
}
