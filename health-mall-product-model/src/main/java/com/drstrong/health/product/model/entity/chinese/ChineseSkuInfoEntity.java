package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 中药 sku 信息表
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_chinese_sku_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChineseSkuInfoEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 老的中药材表主键id，兼容老数据需要
     */
    private Long oldMedicineId;

    /**
     * 药材库code
     */
    private String medicineCode;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 价格/克,单位：分
     */
    private Long price;

    /**
     * sku上下架状态；0-未上架，1-已上架
     */
    private Integer skuStatus;

    /**
     * 剂量类型 0-无限制 1-按倍数限制
     */
    private Integer dosageType;

    /**
     * 倍数值
     */
    private Integer dosageValue;
}
