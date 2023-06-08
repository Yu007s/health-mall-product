package com.drstrong.health.product.model.entity.medication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 西/成药品库
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_western_medicine")
public class WesternMedicineEntity extends BaseStandardEntity implements Serializable {


    private static final long serialVersionUID = 1386077600265463458L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * cspu药品名称
     */
    private String medicineName;

    /**
     * 药品完整名称 拼接规则：品牌 商品名 通用名
     */
    private String fullName;

    /**
     * 通用名
     */
    private String commonName;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 药品编号
     */
    private String medicineCode;

    /**
     * 汉语简拼
     */
    private String pinyin;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 化学名称
     */
    private String chemicalName;

    /**
     * 分类信息，json
     */
    private String medicineClassificationInfo;

    /**
     * 药品本位码
     */
    private String standardCode;

    /**
     * 批准文号/注册证号
     */
    private String approvalNumber;
}
