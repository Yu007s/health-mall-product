package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品分类属性字典表
 * <p> 之前的数据表,直接从 ehp 迁移过来 </>
 *
 * @author liuqiuyi
 * @date 2021/12/27 11:12
 */
@Data
@TableName("p_category_attribute")
public class CategoryAttributeEntity implements Serializable {
	private static final long serialVersionUID = 6905010142412537948L;

	/**
	 * 规格属性
	 */
	public static final Integer ATTR_TYPE_SPECIFICATION = 1;
	/**
	 * 特有属性
	 */
	public static final Integer ATTR_TYPE_DISTINCTIVE = 2;

	@ApiModelProperty(value = "属性键")
	private String attributeKey;

	@ApiModelProperty(value = "属性名称")
	private String attributeName;

	@ApiModelProperty(value = "属性类型：1-规格属性，2-特有属性")
	private Integer attributeType;

	@ApiModelProperty(value = "表单类型：1-文本，2-单选，3-多选，4-日期")
	private Integer formType;

	@ApiModelProperty(value = "属性值选项列表，以逗号分隔（单选和多选才有）")
	private String attributeOptions;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "主键ID(部门Id)")
	private Long id;

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
	@Version
	@JsonIgnore
	private Integer version;
}
