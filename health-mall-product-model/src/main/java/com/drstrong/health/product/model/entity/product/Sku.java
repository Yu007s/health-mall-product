package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sku
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("p_sku")
public class Sku extends BaseEntity implements java.io.Serializable {
    private static final long serialVersionUID = 61284762700205449L;
    
    /**状态：下架 */
    public static final Integer STATUS_OFFLINE = 0;
    /**状态：上架 */
    public static final Integer STATUS_ONLINE = 1;
    private Long id;
    /**
     * 商品id
     */
    private Long productId;

    /**
     * SKU编号
     */
    private String number;

    /**
     * 是否默认  0：是  1：否
     */
    private Integer defaultSku;

    /**
     * sku名称（商品名 通用名 规格）
     */
    private String name;

    /**
     * 包装规格(30mg*6片/盒)
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
     * 单位包装规格数量（180）
     */
    private Integer packingUnitNumber;

    /**
     * 包装规格单位（板，包，瓶）
     */
    private String specUnit;

    /**
     * 包装规格值
     */
    private Integer specValue;
    
    /**
     * 规格
     */
    private String specification;

    /**
     * 售价(分)
     */
    private Integer salePrice;

    /**
     * 重量(克)
     */
    private Integer weight;

    /**
     * 状态(0:下架，1:正常)
     */
    private Integer status;
    private Integer commAttribute;
}
