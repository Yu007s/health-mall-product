package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础字段
 *
 * @author liuqiuyi
 * @date 2021/12/9 14:16
 */
@Data
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = -7417090414497942825L;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createdAt;

	/**
	 * 创建人
	 */
	private String createdBy;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime changedAt;

	/**
	 * 修改人
	 */
	private String changedBy;

	/**
	 * 是否删除 0：正常 1：删除
	 */
	@TableLogic
	private Integer delFlag;
}
