package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品 spu 的基础信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 20:52
 */
@Data
@ApiModel("商品 spu 基础信息")
public class SpuBaseInfoVO implements Serializable {
	private static final long serialVersionUID = -2458334369894407903L;

	@ApiModelProperty("商品编码")
	private String spuCode;

	@ApiModelProperty("商品标题")
	private String productName;

	@ApiModelProperty("商品主图片集合")
	private List<String> imageUrlList;

	@ApiModelProperty("商品详情图片集合")
	private List<String> detailUrlList;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("该商品的 sku 库存总和")
	private Long inventoryNum;
}
