package com.drstrong.health.product.model.entity.category.v3;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.BaseTree;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 老的分类表,整体迁移过来
 *
 * @author liuqiuyi
 * @date 2023/6/12 14:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_category")
@ApiModel(description = "展示分类分类")
public class CategoryEntity extends BaseTree implements Serializable {
	private static final long serialVersionUID = 8225331950302133356L;

	/**
	 * 中西药品顶级父分类ID值（新版本）
	 */
	public static final Long MEDICINE_TOP_PARENT_ID_NEW = 0L;
	/**
	 * 中西药品顶级父分类ID值（旧版本）
	 */
	public static final Long MEDICINE_TOP_PARENT_ID_OLD = -1L;
	/**
	 * 健康用品顶级父分类ID值：旧版本
	 */
	public static final Long HEALTH_PRODUCT_TOP_PARENT_ID = -2L;

	/**
	 * 最大分类级别
	 */
	public static final Integer MAX_LEVEL = 3;

	public static final Integer STATUS_DISABLE = 0;
	public static final Integer STATUS_ENABLE = 1;

	@ApiModelProperty(value = "分类名")
	private String name;

	@ApiModelProperty(value = "节子结点数量")
	private Integer leafCount = 0;

	@ApiModelProperty(value = "路径ID列表，以逗号分隔")
	private String idPath;

	@ApiModelProperty(value = "路径名称列表，以逗号分隔")
	private String namePath;

	@ApiModelProperty(value = "分类级别：以1开始")
	private Integer level = 0;

	@ApiModelProperty(value = "排序号")
	private Integer orderNumber = 0;

	@ApiModelProperty(value = "状态：1-启用，0-禁用, -1-删除")
	private Integer status;

	@ApiModelProperty(value = "已关联商品数量")
	private Integer pNumber = 0;

	@ApiModelProperty(value = "icon图标")
	private String icon;

	@ApiModelProperty(value = "描述")
	private String description;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private Date createdAt;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人")
	private String createdBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改人")
	private String changedBy;

	/**
	 * 修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改时间")
	private Date changedAt;

	/**
	 * 乐观锁
	 */
	private Integer version;
}
