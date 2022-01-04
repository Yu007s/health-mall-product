package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品的 sku 信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_sku")
public class ProductSkuEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 8589920094631828837L;

	/**
	 * 自增主键 id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

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
	 * 乐观锁字段
	 */
	@Version
	private Integer version;
}
