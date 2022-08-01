package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * sku信息表
 * </p>
 *
 * @author mybatis plus generator
 * @since 2022-08-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pms_sku_info")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkuInfoEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * sku编码
     */
    private String skuCode;

    /**
     * sku类型。0-商品，1-药品，2-中药
     * {@link com.drstrong.health.product.model.enums.ProductTypeEnum}
     */
    private Integer skuType;

    /**
     * spu编码
     */
    private String spuCode;

    /**
     * 版本号
     */
    private Integer version;

}
