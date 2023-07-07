package com.drstrong.health.product.model.entity.activty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/7 13:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_activity_package_sku", autoResultMap = true)
public class ActivityPackageSkuInfoEntity  extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 3946900633740078345L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动套餐编码(T开头)
     */
    private String activityPackageCode;

    /**
     * sku编码 商品-p开头，药品-m开头，中药-z开头，协定方-x开头
     */
    private String skuCode;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * 原价，单位：分
     */
    private Long originalPrice;

    /**
     * 优惠价，单位：分
     */
    private Long preferentialPrice;

    /**
     * sku数量
     */
    private Integer amount;
}
