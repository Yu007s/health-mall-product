package com.drstrong.health.product.model.dto.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.drstrong.health.product.model.entity.activty.ActivityPackageSkuInfoEntity;
import io.swagger.annotations.ApiModel;
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
 * 2023/7/7 11:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("活动套餐sku的详细信息")
public class PackageSkuDetailDTO implements Serializable {

    private static final long serialVersionUID = 3214893513730756544L;

    /**
     * sku编码 商品-p开头，药品-m开头，中药-z开头，协定方-x开头
     */
    @ApiModelProperty("sku编码")
    private String skuCode;

    /**
     * sku名称
     */
    @ApiModelProperty("sku名称")
    private String skuName;

    /**
     * 原价
     */
    @ApiModelProperty("原价")
    private Long originalPrice;

    /**
     * 优惠价
     */
    @ApiModelProperty(" 优惠价")
    private Long preferentialPrice;

    /**
     * sku数量
     */
    @ApiModelProperty("数量")
    private Integer amount;

}
