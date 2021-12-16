package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 商品关联的属性值
 *
 * @author liuqiuyi
 * @date 2021/12/13 16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_attribute")
public class ProductAttributeEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1253362077327509712L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 商品id
	 */
	private Long productId;

	/**
	 * 分类属性项id
	 */
	private Long attributeItemId;

	/**
	 * 属性值
	 */
	private String attributeValue;

	/**
	 * 乐观锁
	 */
	private Integer version;
}
