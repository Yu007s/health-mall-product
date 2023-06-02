package com.drstrong.health.product.model.dto.label;

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
 * @date 2023/5/31 17:17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("标签 DTO")
public class LabelDTO implements Serializable {
	private static final long serialVersionUID = 1961302019690514546L;

	@ApiModelProperty("标签 id")
	private Long labelId;

	@ApiModelProperty("标签名称")
	private String labelName;
}
