package com.drstrong.health.product.model.request.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改 sku 的状态
 *
 * @author liuqiuyi
 * @date 2021/12/7 11:31
 */
@Data
@ApiModel("修改 sku 状态的入参")
public class UpdateSkuRequest implements Serializable {
	private static final long serialVersionUID = 8121381019428398949L;

	@ApiModelProperty("上下架状态 0-下架，1-上架")
	private Integer state;

	@ApiModelProperty("skuId 集合")
	private List<Long> skuIdList;
}
