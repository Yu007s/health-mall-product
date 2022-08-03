package com.drstrong.health.product.model.entity.category;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标准的基础字段
 * <p> 由于一些历史原因，之前 BaseEntity 对象的 createdBy 和 changedBy 是 string类型的，出于之后的标准考虑，需要统一修改为 Long 类型</>
 *
 * @author liuqiuyi
 * @date 2021/12/9 14:16
 */
@Data
public class BaseStandardEntity implements Serializable {
    private static final long serialVersionUID = -7417090414497942825L;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changedAt;

    /**
     * 修改人
     */
    private Long changedBy;

    /**
     * 版本号
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 是否删除 0：正常 1：删除
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;
}
