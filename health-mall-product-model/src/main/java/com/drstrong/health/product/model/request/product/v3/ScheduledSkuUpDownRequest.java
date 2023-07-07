package com.drstrong.health.product.model.request.product.v3;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
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

	/**
	 * 定时上架
	 */
	public static final int SCHEDULED_UP = 2;

	/**
	 * 定时下架
	 */
	public static final int SCHEDULED_DOWN = 3;


	@ApiModelProperty("2-定时上架,3-定时下架")
	@NotNull(message = "定时上下架配置不能为空")
	@Range(min = 2, max = 3, message = "定时上下架配置传参不正确")
	private Integer scheduledType;

	@ApiModelProperty("时间")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Future(message = "上下架时间必须晚于当前时间")
	@NotNull(message = "上下架时间不能为空")
	private LocalDateTime scheduledDateTime;
}
