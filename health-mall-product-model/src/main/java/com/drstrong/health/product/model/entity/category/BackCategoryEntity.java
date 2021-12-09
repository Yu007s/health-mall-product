package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 后台分类
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_product_back_category")
public class BackCategoryEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -1581848198754072917L;

	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 上级分类id，为0代表当前分类为一级
	 */
	private Long parentId;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 叶子节点分类数量
	 */
	private Integer leafCount;

	/**
	 * 路径id列表，以逗号分隔
	 */
	private String idPath;

	/**
	 * 路径名称列表，以逗号分隔
	 */
	private String namePath;

	/**
	 * 分类级别
	 */
	private Integer level;

	/**
	 * 排序号
	 */
	private Integer orderNumber;

	/**
	 * 状态：1-启用，0-禁用， -1-删除
	 */
	private Integer status;

	/**
	 * icon图标
	 */
	private String icon;

	/**
	 * 已关联商品数量
	 */
	private Integer pNumber;

	/**
	 * 描述
	 */
	private String description;
}
