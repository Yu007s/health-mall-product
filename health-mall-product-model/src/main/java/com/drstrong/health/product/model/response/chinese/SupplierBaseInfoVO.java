package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2022/8/8 13:59
 */
@Data
@ApiModel("供应商基础信息")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierBaseInfoVO implements Serializable {
	private static final long serialVersionUID = -2091597682405619203L;

	@ApiModelProperty("供应商 id")
	private Long supplierId;

	@ApiModelProperty("供应商名称")
	private String supplierName;
}
