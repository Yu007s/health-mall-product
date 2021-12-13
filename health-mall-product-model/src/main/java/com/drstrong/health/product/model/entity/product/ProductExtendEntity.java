package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品扩展信息
 *
 * @author liuqiuyi
 * @date 2021/12/13 15:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_extend")
public class ProductExtendEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 2416515763289435433L;

	/**
	 * 自增主键
	 */
	@TableId
	private Long id;

	/**
	 * 商品 id
	 */
	private Long productId;

	/**
	 * 商品描述
	 */
	private String description;

	/**
	 * 批文编号
	 */
	private String approvalNumber;

	/**
	 * 生产厂商名称
	 */
	private String vendorName;

	/**
	 * 商品页图片地址
	 */
	private String imageUrl;

	/**
	 * 详情页图片信息
	 */
	private String detailUrl;

	/**
	 * 乐观锁字段
	 */
	private Integer version;
}
