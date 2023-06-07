package com.drstrong.health.product.model.entity.medication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 西/成药品说明
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_western_medicine_instructions")
public class WesternMedicineInstructionsEntity extends BaseStandardEntity implements Serializable {


    private static final long serialVersionUID = -8730840716652986174L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 西药id
     */
    private Long medicineId;

    /**
     * 成分
     */
    private String ingredients;

    /**
     * 性状
     */
    private String phenotypicTrait;

    /**
     * 适应症/功能主治
     */
    private String indications;

    /**
     * 用法用量
     */
    private String usageDosage;

    /**
     * 不良反应
     */
    private String adverseEffects;

    /**
     * 禁忌
     */
    private String contraindications;

    /**
     * 注意事项
     */
    private String mattersNeedingAttention;

    /**
     * 药品贮藏
     */
    private String medicineStorage;

    /**
     * 生产企业
     */
    private String productionEnterprise;

    /**
     * 上市许可持有人
     */
    private String listingLicensee;

    /**
     * 有效期
     */
    private String periodValidity;

    /**
     * 执行标准
     */
    private String executionStandard;
}
