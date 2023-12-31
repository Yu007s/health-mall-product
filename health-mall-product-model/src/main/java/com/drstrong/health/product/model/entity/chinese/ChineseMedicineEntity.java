package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
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
public class ChineseMedicineEntity extends BaseStandardEntity implements Serializable {

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
     * 药材编码
     */
    @TableField("medicine_code")
    private String medicineCode;


    /**
     * 药材名字汉语拼音
     */
    @TableField("medicine_pinyin")
    private String medicinePinyin;

    /**
     * 药材别名汉语拼音
     */
    @TableField("alias_pinyin")
    private String aliasPinyin;

    /**
     * 汉语别名以“,”隔开
     */
    @TableField("medicine_alias")
    private String medicineAlias;

    /**
     * 最大剂量
     */
    @TableField("max_dosage")
    private BigDecimal maxDosage;

    /**
     * 剂型 0-配方颗粒 1-饮片 默认0
     */
    @TableField("dosage_form")
    private Integer dosageForm;

}
