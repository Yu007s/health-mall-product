package com.drstrong.health.product.model.dto.label;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author liuqiuyi
 * @date 2023/6/1 11:10
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("标签信息扩展")
public class LabelExtendDTO extends LabelDTO {
	private static final long serialVersionUID = -3151188377260654825L;

	@ApiModelProperty("创建人 id")
	private Long createdBy;

	@ApiModelProperty("创建人名称")
	private String createdName;

	@ApiModelProperty("修改时间")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@ApiModelProperty("修改人 id")
	private Long changedBy;

	@ApiModelProperty("修改时间")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime changedAt;
}
