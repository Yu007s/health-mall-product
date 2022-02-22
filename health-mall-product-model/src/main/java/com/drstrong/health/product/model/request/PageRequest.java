package com.drstrong.health.product.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询的公共入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 19:35
 */
@Data
@ApiModel("分页查询的公共入参")
public class PageRequest implements Serializable {
	private static final long serialVersionUID = 670302761069367870L;

	@ApiModelProperty("查询的页码")
	private Integer pageNo = 1;

	@ApiModelProperty("查询的条数")
	private Integer pageSize = 10;
}
