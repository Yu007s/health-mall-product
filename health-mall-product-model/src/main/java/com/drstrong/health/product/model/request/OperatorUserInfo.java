package com.drstrong.health.product.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/6 14:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("操作人相关信息")
public class OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = -898476181599459642L;

	@ApiModelProperty(value = "操作人 id", hidden = true)
	private Long operatorId;

	@ApiModelProperty(value = "操作人Name", hidden = true)
	private String operatorName;
}
