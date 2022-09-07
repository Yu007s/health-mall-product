package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2022/8/5 10:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("供应商中药库存页面的返回值")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupplierChineseManagerSkuVO extends ChineseManagerSkuVO implements Serializable {
	private static final long serialVersionUID = -4911971288386490972L;

	@ApiModelProperty("供应商 id")
	private Long supplierId;
}
