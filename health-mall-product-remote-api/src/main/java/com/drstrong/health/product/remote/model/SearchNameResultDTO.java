package com.drstrong.health.product.remote.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询商品名称的返回值
 *
 * @author liuqiuyi
 * @date 2021/12/27 20:16
 */
@Data
@ApiModel(value = "查询商品名称的返回值", description = "和之前空中药房的返回值保持一致")
public class SearchNameResultDTO implements Serializable {
	private static final long serialVersionUID = -9111805603357882788L;

	@ApiModelProperty("品牌名称")
	private String name;

	@ApiModelProperty("商品 spu 名称")
	private String commonName;
}
