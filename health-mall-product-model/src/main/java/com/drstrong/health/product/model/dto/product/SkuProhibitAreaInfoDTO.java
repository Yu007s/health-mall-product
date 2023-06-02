package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/1 11:34
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku禁售区域的DTO信息")
public class SkuProhibitAreaInfoDTO extends SkuBaseDTO implements Serializable {
	private static final long serialVersionUID = 3611142423005345123L;

	@ApiModelProperty("区域id")
	private Long areaId;

	@ApiModelProperty("区域名称")
	private String areaName;
}
