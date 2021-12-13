package com.drstrong.health.product.model.response.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品管理信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 14:43
 */
@Data
@ApiModel("商品的管理信息")
public class ProductManageVO implements Serializable {
	private static final long serialVersionUID = -844091335147592121L;

	@ApiModelProperty("商品 id")
	private Long productId;

	@ApiModelProperty("spu 编码")
	private String spuCode;

	@ApiModelProperty("分类 id")
	private Long categoryId;

	@ApiModelProperty("分类名称")
	private String categoryPathName;

	@ApiModelProperty("商品的标题")
	private String title;

	@ApiModelProperty("品牌名称")
	private String brandName;

	@ApiModelProperty("商品通用名称")
	private String aliasName;

	@ApiModelProperty("商品描述")
	private String description;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("店铺名称")
	private String storeName;

	@ApiModelProperty("商品的主图 url 集合")
	private List<String> imageUrlList;

	@ApiModelProperty("商品详情页的图片")
	private List<String> detailUrlList;

	@ApiModelProperty("商品属性集合")
	private List<PropertyInfoResponse> propertyValueList;

	@ApiModelProperty("商品规格集合")
	private List<PackInfoResponse> packInfoList;
}
