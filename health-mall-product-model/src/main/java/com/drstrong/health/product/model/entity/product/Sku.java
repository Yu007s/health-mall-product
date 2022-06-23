package com.drstrong.health.product.model.entity.product;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * sku
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("p_sku")
public class Sku implements java.io.Serializable {
    private static final long serialVersionUID = 61284762700205449L;
    
    /**状态：下架 */
    public static final Integer STATUS_OFFLINE = 0;
    /**状态：上架 */
    public static final Integer STATUS_ONLINE = 1;

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
    @TableId
    @ApiModelProperty(value = "主键ID(部门Id)")
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改人")
    private String changedBy;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改时间")
    private Date changedAt;

    /**
     * 乐观锁
     */
    @Version
    @JsonIgnore
    private Integer version;
}
