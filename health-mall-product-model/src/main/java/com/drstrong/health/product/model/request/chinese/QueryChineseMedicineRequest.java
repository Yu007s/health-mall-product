package com.drstrong.health.product.model.request.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2022/8/10 14:59
 */
@Data
@ApiModel("查询中药的入参信息")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryChineseMedicineRequest implements Serializable {
	private static final long serialVersionUID = 916863307157501743L;

	@ApiModelProperty("搜索关键字")
	private String keyword;
}
