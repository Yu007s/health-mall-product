package com.drstrong.health.product.model.dto;

import lombok.Data;

/**
 * 公共的 sku 对象
 * @author liuqiuyi
 * @date 2021/12/15 21:15
 */
@Data
public class ProductSkuDTO {
	/**
	 * 自增主键 id
	 */
	private Long skuId;

	/**
	 * 商品 id
	 */
	private Long productId;

	/**
	 * sku 名称
	 */
	private String skuName;

	/**
	 * sku编码
	 */
	private String skuCode;

	/**
	 * 来源 id,就是店铺 id
	 */
	private Long sourceId;

	/**
	 * 来源名称,就是店铺名称
	 */
	private String sourceName;

	/**
	 * 规格名称
	 */
	private String packName;

	/**
	 * 规格值
	 */
	private String packValue;

	/**
	 * sku价格
	 */
	private Integer skuPrice;

	/**
	 * 上架状态(0-未上架,1-已上架)
	 */
	private Integer state;

	/**
	 * 商品属性 以数字字典配置为准
	 */
	private Integer commAttribute;

	/**
	 * 商品属性名称
	 */
	private String commAttributeName;
}
