package com.drstrong.health.product.model.entity.medication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
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
    private Integer type;

    /**
     * 几月、几日 、几小时:有0.5的情况
     */
    private Double dosageCycle;

    /**
     * 单位：日、隔日、小时、周、月等
     */
    private String dosageCycleUnit;

    /**
     * 次数:有若干次的情况
     */
    private Integer dosageCount;

    /**
     * 每次几片,几毫克,几粒等等:有适量的情况
     */
    private String eachDosageCount;

    /**
     * 药品单位
     */
    private String eachDoseUnit;

    /**
     * 最小规格包装数（如：一盒有多少片 ）
     */
    private Double packagSpec;

    /**
     * 药品单位:盒、瓶
     */
    private String quantityUnit;

    /**
     * 服用方式
     */
    private String usageMethod;

    /**
     * 服用时间
     */
    private String usageTime;

}