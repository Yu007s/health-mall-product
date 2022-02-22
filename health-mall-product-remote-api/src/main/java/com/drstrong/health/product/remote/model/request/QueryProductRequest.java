package com.drstrong.health.product.remote.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 查询商品入参
 *
 * @author liuqiuyi
 * @date 2021/12/29 14:51
 */
@Data
@ApiModel("查询商品信息入参")
public class QueryProductRequest implements Serializable {
	private static final long serialVersionUID = 3162755993602053374L;

	@ApiModelProperty("skuId 集合,和 skuCodeList 不能同时为空")
	private Set<Long> skuIdList;

	@ApiModelProperty("sku 编码,和 skuIdList 不能同时为空")
	private Set<String> skuCodeList;

	@ApiModelProperty("商品 sku 上下架状态 0-未上架,1-已上架")
	private Integer upOffStatus;
}
