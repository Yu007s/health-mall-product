package com.drstrong.health.product.model.entity.medication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 协定方(预制)
 * </p>
 *
 * @author zzw
 * @since 2023-06-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_agreement_prescription_medicine")
public class AgreementPrescriptionMedicineEntity extends BaseStandardEntity implements Serializable{


    private static final long serialVersionUID = 3306377493572110372L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 协定方编号
     */
    private String medicineCode;

    /**
     * 协定方名称
     */
    private String medicineName;

    /**
     * 分类信息，json
     */
    private String medicineClassificationInfo;

    /**
     * 规格包装(0.25g*12片*2板/盒)
     */
    private String packingSpec;

    /**
     * 外包装单位（盒,瓶...)
     */
    private String packingUnit;

    /**
     * 最小包装单位（片,粒...）
     */
    private String packingUnitLimit;

    /**
     * 单位包装规格数量（24）
     */
    private Integer packingUnitNumber;

    /**
     * 拆分单位
     */
    private String splitUnit;

    /**
     * 拆分数量
     */
    private Integer splitValue;

    /**
     * 处方
     */
    private String prescriptions;

    /**
     * 功效
     */
    private String efficacy;

    /**
     * 服法
     */
    private String usageMethod;

}
