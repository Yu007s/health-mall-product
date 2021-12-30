package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 前后台分类关联表
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_category_relation")
public class CategoryRelationEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -2114710950763193867L;

	/**
	 * 自增主键 id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 前台商品分类 id
	 */
	private Long frontCategoryId;

	/**
	 * 后台商品分类 id
	 */
	private Long backCategoryId;

	/**
	 * 分类状态(1-启用;0-禁用)
	 */
	private Integer state;

	/**
	 * 乐观锁字段
	 */
	private Integer version;
}
