package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 前台分类
 *
 * @author liuqiuyi
 * @date 2021/12/7 17:21
 */

@Data
@TableName("pms_product_front_category")
public class FrontCategoryEntity implements Serializable {
	private static final long serialVersionUID = -1403402300574048172L;

	/**
	 * 自增主键 id
	 */
	private Long id;

	/**
	 * 父类 id(一级父类 id 为 0)
	 */
	private Integer parentId;

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
	 * 删除状态(1-已删除;0-未删除)
	 */
	private Integer deleteStatus;

	/**
	 * 创建人 id
	 */
	private Integer createdBy;

	/**
	 * 创建时间
	 */
	private LocalDateTime createdTime;

	/**
	 * 修改人 id
	 */
	private Integer updatedBy;

	/**
	 * 修改时间
	 */
	private LocalDateTime updatedTime;
}