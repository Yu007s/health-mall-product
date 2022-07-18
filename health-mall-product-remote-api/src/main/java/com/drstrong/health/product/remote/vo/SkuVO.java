package com.drstrong.health.product.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * sku管理，保存和修改，都使用此vo
 */
@Data
@ApiModel(description = "SKU详情")
public class SkuVO implements java.io.Serializable {

    private static final long serialVersionUID = 1786318348277717993L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 商品id
     */
    @NotNull(message = "商品ID不能为null")
    @ApiModelProperty(value = "商品id")
    private Long productId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String number;

    @NotNull(message = "是否默认不能为null")
    @ApiModelProperty(value = "是否默认  0：是  1：否")
    private Integer defaultSku;

    /**
     * sku名称（商品名 通用名 规格）
     */
    @NotEmpty(message = "sku名称不能为null")
    @ApiModelProperty(value = "sku名称（商品名 通用名 规格）")
    private String name;

    /**
     * 包装规格(30mg*6片/盒)
     */
    @NotEmpty(message = "包装规格不能为null")
    @ApiModelProperty(value = "包装规格(30mg*6片/盒)")
    private String packingSpec;

    @ApiModelProperty(value = "外包装单位（盒,瓶...)")
    private String packingUnit;
    
    @ApiModelProperty(value = "最小包装单位（片,粒...）")
    private String packingUnitLimit;
    
    /**
     * 单位包装规格数量（24）
     */
    @NotNull(message = "单位包装规格数量不能为null")
    @ApiModelProperty(value = "单位包装规格数量（24")
    private Integer packingUnitNumber;

    @ApiModelProperty(value = "规格单位（板，包，瓶）")
    private String specUnit;

    @ApiModelProperty(value = "包装规格值")
    private Integer specValue;

    @ApiModelProperty(value = "规格")
    private String specification;
    
    /**
     * 售价(分)
     */
    @NotNull(message = "售价不能为null")
    @Min(value = 0, message = "售价不能小于0")
    @ApiModelProperty(value = "售价")
    private Integer salePrice;

    /**
     * 重量(克)
     */
    @NotNull(message = "重量不能为null")
    @Min(value = 0, message = "重量不能小于0")
    @ApiModelProperty(value = "重量")
    private Integer weight;

    /**
     * 状态(0:下架，1:正常)
     */
    @ApiModelProperty(value = "状态(0:下架，1:正常)")
    private Integer status;

    @ApiModelProperty(value = "图片列表")
    private List<String> images;
    
    @ApiModelProperty(value = "关联药房ID")
    private Long warehouseId;

    private Integer commAttribute;

}
