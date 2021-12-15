package com.drstrong.health.product.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2021/12/15 21:12
 */
@Data
public class ProductBasicsInfoDTO implements Serializable {
	private static final long serialVersionUID = 554308638688299202L;
	/**
	 * 自增主键
	 */
	private Long productId;

	/**
	 * spu编码
	 */
	private String spuCode;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 品牌名称
	 */
	private String brandName;

	/**
	 * 商品别名
	 */
	private String aliasName;

	/**
	 * 主图地址
	 */
	private String masterImageUrl;

	/**
	 * 商品后端分类 id
	 */
	private Long categoryId;

	/**
	 * 商品来源(区分不同的店铺来源)
	 */
	private Long sourceId;

	/**
	 * 商品来源名称(冗余字段)
	 */
	private String sourceName;

	/**
	 * 商品类型(0-商品，1-药品)
	 */
	private Integer productType;

	/**
	 * 排序字段(值越大优先级越高)
	 */
	private Integer sort;

	/**
	 * 上架状态(0-未上架，1-已上架)
	 */
	private Integer state;

	/**
	 * 商品的 sku 集合
	 */
	private List<ProductSkuDTO> skuList;
}
