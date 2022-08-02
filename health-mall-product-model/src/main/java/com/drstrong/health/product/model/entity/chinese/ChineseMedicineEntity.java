package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 中药材库
 * </p>
 *
 * @author xieYueFeng
 * @since 2022-07-207 17:12:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product_chinese_medicine")
public class ChineseMedicineEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1458548948245945617L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 药材名称
     */
    @TableField("medicine_name")
    private String medicineName;

    /**
     * 药材名称
     */
    @TableField("medicine_code")
    private Long medicineCode;


    /**
     * 汉语简拼
     */
    @TableField("pinyin")
    private String pinyin;


    /**
     * 最大剂量
     */
    @TableField("max_dosage")
    private BigDecimal maxDosage;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;


}
