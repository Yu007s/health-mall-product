package com.drstrong.health.product.model.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 保存商品信息
 *
 * @author liuqiuyi
 * @date 2021/12/6 14:23
 */
@Data
@ApiModel("保存商品信息的入参")
public class SaveProductRequest implements Serializable {
	private static final long serialVersionUID = -4015905101502960465L;

	@ApiModelProperty(value = "分类 id", notes = "保存商品信息时非必填,更新商品信息时该值必填")
	private Long productId;

	@ApiModelProperty(value = "分类 id", required = true)
	@NotNull(message = "categoryId 不能为空")
	private Long categoryId;

	@ApiModelProperty(value = "商品名称", required = true)
	@NotEmpty(message = "title 不能为空")
	@Size(max = 60, message = "商品名称长度不能大于 60")
	private String title;

	@ApiModelProperty(value = "商品品牌", required = true)
	@NotEmpty(message = "brandName 不能为空")
	@Size(max = 60, message = "品牌名称长度不能大于 60")
	private String brandName;

	@ApiModelProperty(value = "商品通用名", required = true)
	@NotEmpty(message = "aliasName 不能为空")
	@Size(max = 60, message = "商品通用名长度不能大于 60")
	private String aliasName;

	@ApiModelProperty("商品描述")
	@Size(max = 60, message = "商品描述长度不能大于 60")
	private String description;

	@ApiModelProperty(value = "批文编号", required = true)
	@NotEmpty(message = "approvalNumber 不能为空")
	@Size(max = 100, message = "批文编号长度不能大于 100")
	private String approvalNumber;

	@ApiModelProperty(value = "生产厂商", required = true)
	@NotEmpty(message = "vendorName 不能为空")
	@Size(max = 200, message = "生产厂商长度不能大于 200")
	private String vendorName;

	@ApiModelProperty(value = "店铺 id", required = true)
	@NotNull(message = "storeId 不能为空")
	private Long storeId;

	@ApiModelProperty("商品属性")
	private List<PropertyInfoRequest> propertyValueList;

	@ApiModelProperty(value = "商品规格信息", required = true)
	@NotNull(message = "packInfoList 不能为空")
	@Size(max = 20, message = "商品规格信息最多不超过 20 条")
	@Valid
	private List<PackInfoRequest> packInfoList;

	@ApiModelProperty(value = "商品的主图 url 集合", required = true)
	@NotNull(message = "imageUrlList 不能为空")
	@Size(min = 1, max = 5, message = "商品主图最少上传 1 张,最多上传 5 张")
	private List<String> imageUrlList;

	@ApiModelProperty(value = "商品详情页的图片", required = true)
	@NotNull(message = "detailUrlList 不能为空")
	private List<String> detailUrlList;

	@ApiModelProperty(value = "操作人 id", hidden = true)
	private String userId;

	@Data
	@ApiModel("商品属性的入参")
	public static class PropertyInfoRequest implements Serializable {
		private static final long serialVersionUID = 1827746211368213777L;

		@ApiModelProperty("属性 id")
		private Long attributeItemId;

		@ApiModelProperty("属性名称")
		private String attributeValue;
	}

	@Data
	@ApiModel("商品规格的入参")
	public static class PackInfoRequest implements Serializable {
		private static final long serialVersionUID = 4311064924147174227L;

		@ApiModelProperty(value = "skuId", notes = "保存商品规格信息时非必填,更新商品规格信息时该值必填")
		private Long skuId;

		@ApiModelProperty("规格名称")
		@NotEmpty(message = "packName 不能为空")
		@Size(max = 12, message = "规格名称长度不能大于 12")
		private String packName;

		@ApiModelProperty("规格值")
		@NotEmpty(message = "packValue 不能为空")
		@Size(max = 50, message = "规格值长度不能大于 50")
		private String packValue;

		@ApiModelProperty("商品价格")
		@NotNull(message = "price 不能为空")
		@Max(value = 999999, message = "价格最高不超过 6 位数")
		private BigDecimal price;

		@ApiModelProperty("属性 id")
		@NotNull(message = "commAttribute 不能为空")
		private Integer commAttribute;
	}
}
