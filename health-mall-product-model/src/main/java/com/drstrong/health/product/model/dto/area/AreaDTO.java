package com.drstrong.health.product.model.dto.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/10 15:16
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("区域信息")
public class AreaDTO implements Serializable {
	private static final long serialVersionUID = -8817341427437065160L;

	@ApiModelProperty("区域 id")
	private Long areaId;

	@ApiModelProperty("区域名称")
	private String areaName;
}
