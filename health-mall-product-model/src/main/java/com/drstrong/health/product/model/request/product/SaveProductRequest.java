package com.drstrong.health.product.model.request.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

	@ApiModelProperty(value = "分类 id", required = true)
	@NotNull(message = "categoryId 不能为空")
	private Long categoryId;

	@ApiModelProperty(value = "商品标题", required = true)
	@NotEmpty(message = "title 不能为空")
	private String title;

	@ApiModelProperty("商品描述")
	private String description;

	@ApiModelProperty(value = "店铺 id", required = true)
	@NotNull(message = "storeId 不能为空")
	private Long storeId;

	@ApiModelProperty("商品属性")
	private List<PropertyInfoRequest> propertyValueList;

	@ApiModelProperty(value = "商品规格信息", required = true)
	@NotNull(message = "packInfoList 不能为空")
	@Valid
	private List<PackInfoRequest> packInfoList;

	@ApiModelProperty(value = "商品的主图 url 集合", required = true)
	@NotNull(message = "imageUrlList 不能为空")
	private List<String> imageUrlList;

	@ApiModelProperty(value = "商品详情页的图片", required = true)
	@NotNull(message = "detailUrlList 不能为空")
	private List<String> detailUrlList;

	@Data
	@ApiModel("商品属性的入参")
	private static class PropertyInfoRequest implements Serializable {
		private static final long serialVersionUID = 1827746211368213777L;

		@ApiModelProperty("属性 id")
		private Long propertyId;

		@ApiModelProperty("属性名称")
		private String propertyValue;
	}

	@Data
	@ApiModel("商品规格的入参")
	private static class PackInfoRequest implements Serializable {
		private static final long serialVersionUID = 4311064924147174227L;

		@ApiModelProperty("规格名称")
		@NotEmpty(message = "packName 不能为空")
		private String packName;

		@ApiModelProperty("规格值")
		@NotEmpty(message = "packValue 不能为空")
		private String packValue;

		@ApiModelProperty("商品价格")
		@NotNull(message = "price 不能为空")
		private BigDecimal price;

		@ApiModelProperty("属性 id")
		@NotNull(message = "commAttribute 不能为空")
		private Integer commAttribute;
	}
}
