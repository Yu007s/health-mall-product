package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 前台分类
 *
 * @author liuqiuyi
 * @date 2021/12/7 17:21
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_front_category")
public class FrontCategoryEntity extends BaseTree implements Serializable {
	private static final long serialVersionUID = -1403402300574048172L;

	/**
	 * 分类名称
	 */
	private String name;

	/**
	 * icon 图片地址
	 */
	private String icon;

	/**
	 * 级别，默认为 1
	 */
	private Integer level;

	/**
	 * 排序字段(值越大越优先)
	 */
	private Integer sort;

	/**
	 * 描述字段
	 */
	private String description;

	/**
	 * 分类状态(1-启用;0-禁用)
	 */
	private Integer state;

	/**
	 * 乐观锁字段
	 */
	private Integer version;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdAt;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 修改时间
	 */
	private LocalDateTime changedAt;

	/**
	 * 修改人
	 */
	private String changedBy;

	/**
	 * 是否删除 0：正常 1：删除
	 */
	private Integer delFlag;
}