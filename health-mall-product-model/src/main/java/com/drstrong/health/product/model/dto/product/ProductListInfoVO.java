package com.drstrong.health.product.model.dto.product;

import com.drstrong.health.product.model.dto.medicine.MedicineImageDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * huangpeng
 * 2023/7/11 09:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductListInfoVO implements Serializable {

    private static final long serialVersionUID = 3791913589559446114L;

    /**
     * sku编码
     */
    @ApiModelProperty(value = "sku编码")
    private String skuCode;

    /**
     * sku名称（商品名 通用名 规格）
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;

    /**
     * sku类型(0-商品，1-药品，2-中药,3-协定方)
     */
    @ApiModelProperty(value = "sku类型")
    private Integer skuType;

    /**
     * sku上下架状态；0-已下架，1-已上架, 2-预约上架中, 3-预约下架中
     */
    @ApiModelProperty(value = "sku上下架状态；0-已下架，1-已上架, 2-预约上架中, 3-预约下架中")
    private Integer skuStatus;

    /**
     * 店铺ID
     */
    @ApiModelProperty("店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String storeName;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String company;

    /**
     * 商品封面图片
     */
    @ApiModelProperty(value = "商品封面图片")
    private List<MedicineImageDTO> imageInfo;

    /**
     * 售价
     */
    @ApiModelProperty(value = "售价(元为单位)")
    private BigDecimal salePrice;

    /**
     * 售价值
     */
    @ApiModelProperty(value = "售价值(分为单位)")
    private Integer salePriceValue;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private Long quantity;

    /**
     * 安全类别：相当于药品类别：1-处方药；2-OTC药品
     */
    @ApiModelProperty(value = "药品类别：1，处方药；2，OTC药品")
    private Integer rx;

    /**
     * 药品标签：相当于原来的药品属性，1常用药，2特效药
     */
    @ApiModelProperty(value = "药品标签")
    private Integer medicineAttributeId;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格")
    private String spec;

    /**
     * 用法用量
     */
    @ApiModelProperty(value = "用法用量")
    private String usage;

    /**
     * 常用药标识：0，否；1，是
     */
    @ApiModelProperty(value = "常用药标识：0，否；1，是")
    private Integer boxTag;

    /**
     * 商品关联的套餐信息
     */
    @ApiModelProperty(value = "商品关联的套餐信息")
    private List<PackageInfoVO> packageInfoVOList;
}
