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
 * 西药药品规格
 * </p>
 *
 * @author zzw
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pms_western_medicine_specifications")
public class WesternMedicineSpecificationsEntity extends BaseStandardEntity implements Serializable {


    private static final long serialVersionUID = -5684299008575008500L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 药品ID
     */
    private Long medicineId;

    /**
     * 规格 编码
     */
    private String specCode;

    /**
     * 规格名称（商品名  通用名  规格）
     */
    private String specName;

    /**
     * 包装规格(0.25g*12片*2板/盒)
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
     * 规格单位（板）
     */
    private String specUnit;

    /**
     * 规格值
     */
    private Integer specValue;

    /**
     * 规格
     */
    private String specification;

}
