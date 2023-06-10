package com.drstrong.health.product.model.entity.medication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 药品规格用法用量
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_medicine_usage")
public class MedicineUsageEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = -6225375986337076010L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药品规格id
     */
    private Long specificationsId;

    /**
     * 类型 1：西药 2：协定方
     */
    private Integer medicineType;

    /**
     * 用药频次
     */
    private String medicationFrequency;

    /**
     * 每次几片,几毫克,几粒等等:有适量的情况
     */
    private String eachDosageCount;

    /**
     * 药品单位
     */
    private String eachDoseUnit;

    /**
     * 服用方式
     */
    private String usageMethod;

    /**
     * 服用时间
     */
    private String usageTime;

}
