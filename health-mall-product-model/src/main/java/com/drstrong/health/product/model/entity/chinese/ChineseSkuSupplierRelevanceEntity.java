package com.drstrong.health.product.model.entity.chinese;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 中药sku和供应商关联表
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_chinese_sku_supplier_relevance")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChineseSkuSupplierRelevanceEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * sku 编码
     */
    private String skuCode;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 版本号
     */
    private Integer version;
}
