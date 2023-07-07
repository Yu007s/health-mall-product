package com.drstrong.health.product.model.entity.medication;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.BaseTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 药品基础分类
 * </p>
 *
 * @author zzw
 * @since 2023-06-06
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("pms_medicine_classification")
public class MedicineClassificationEntity extends BaseTree implements Serializable {


    private static final long serialVersionUID = -4044204863617202894L;


    /**
     * 分类名称
     */
    private String classificationName;

    /**
     * 类型：1 药理分类，2型剂分类，3药品分类，4安全分类，5原料分类
     */
    private Integer classificationType;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 版本号
     */
    private Integer version;


    /**
     * 逻辑删除位：0-未删除，1-已删除
     */
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 修改人
     */
    private Long changedBy;

    /**
     * 修改时间
     */
    private LocalDateTime changedAt;

}
