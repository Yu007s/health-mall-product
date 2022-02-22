package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuqiuyi
 * @date 2021/12/13 14:24
 */
@Data
@TableName("p_category_attribute_item")
public class CategoryAttributeItemEntity implements Serializable {
	private static final long serialVersionUID = 4687010081862575043L;

	/**
	 * 规格属性
	 */
	public static final Integer ATTR_TYPE_SPECIFICATION = 1;
	/**
	 * 特有属性
	 */
	public static final Integer ATTR_TYPE_DISTINCTIVE = 2;

	/**
	 *
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 后端分类 id
	 */
	private Long categoryId;
	/**
	 * 分类属性ID
	 */
	private Long attributeId;
	/**
	 * 属性键
	 */
	private String attributeKey;
	/**
	 * 属性名称
	 */
	private String attributeName;
	/**
	 * 属性类型：1-规格属性，2-特有属性
	 */
	private Integer attributeType;
	/**
	 * 表单类型：1-文本，2-单选，3-多选，4-日期
	 */
	private Integer formType;
	/**
	 * 属性值选项列表，以逗号分隔（单选和多选才有）
	 */
	private String attributeOptions;
	/**
	 * 是否必填：0-非必填，1-必填
	 */
	private Integer required = 0;
	/**
	 * 排序号
	 */
	private Integer orderNumber = 0;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 修改时间
	 */
	private Date changedAt;
	/**
	 * 修改人
	 */
	private String changedBy;
	/**
	 * 版本号
	 */
	private Integer version;
}
