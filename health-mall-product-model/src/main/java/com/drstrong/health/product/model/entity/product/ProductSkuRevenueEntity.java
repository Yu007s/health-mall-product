package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * sku 关联的税收编码信息
 *
 * @author liuqiuyi
 * @date 2021/12/14 14:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_sku_revenue")
public class ProductSkuRevenueEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 4983947671096547859L;

	/**
	 * 自增主键 id
	 */
	private Long id;

	/**
	 * 商品 id
	 */
	private Long productId;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * sku编码
	 */
	private String skuCode;

	/**
	 * 税收编码
	 */
	private String revenueCode;

	/**
	 * 税率
	 */
	private Integer revenueRate;

	/**
	 * 乐观锁字段
	 */
	private Integer version;
}
