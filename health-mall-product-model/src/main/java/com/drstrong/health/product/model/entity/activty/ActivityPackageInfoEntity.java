package com.drstrong.health.product.model.entity.activty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.LongListTypeHandler;
import com.drstrong.health.product.handler.mybatis.StringListTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * huangpeng
 * 2023/7/7 11:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "pms_activity_package_manage", autoResultMap = true)
public class ActivityPackageInfoEntity extends BaseStandardEntity implements Serializable {

    private static final long serialVersionUID = 3946900622740078345L;

    /**
     * 套餐种类：目前仅支持 1-西成药
     */
    public static final Integer PACKAGE_TYPE = 1;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动名称
     */
    private String activityPackageName;

    /**
     * 活动套餐编码(T开头)
     */
    private String activityPackageCode;

    /**
     * 商品种类(1-西成药)
     */
    private Integer productType;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 套餐上下架状态；0-已下架，1-已上架, 2-预约上架中, 3-预约下架中
     */
    private int activityStatus;

    /**
     * 活动套餐原价，单位：分
     */
    private Long originalPrice;

    /**
     * 活动套餐优惠价，单位：分
     */
    private Long preferentialPrice;

    /**
     * 页面是否展示原价(0不展示,1展示)
     */
    private Integer originalAmountDisplay;

    /**
     * 活动套餐的图片信息(json存储)
     */
    @TableField(value = "activity_package_image_info", typeHandler = StringListTypeHandler.class)
    private List<String> activityPackageImageInfo;

    /**
     * 活动套餐介绍
     */
    private String activityPackageIntroduce;

    /**
     * 活动套餐备注
     */
    private String activityPackageRemark;
}
