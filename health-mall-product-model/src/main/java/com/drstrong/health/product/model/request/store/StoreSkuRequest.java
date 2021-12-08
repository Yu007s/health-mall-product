package com.drstrong.health.product.model.request.store;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 店铺 sku 的查询入参
 *
 * @author liuqiuyi
 * @date 2021/12/7 10:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("店铺 sku 的查询入参")
public class StoreSkuRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 3750645141030243009L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("sku 编码")
	private String skuCode;

	@ApiModelProperty("sku 名称")
	private String skuName;

	@ApiModelProperty("三方 skuId")
	private String threeSkuId;

	@ApiModelProperty("sku 状态")
	private Integer skuState;
}
