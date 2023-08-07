package com.drstrong.health.product.model.entity.activty;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.drstrong.health.product.handler.mybatis.LongListTypeHandler;
import com.drstrong.health.product.handler.mybatis.StringListTypeHandler;
import com.drstrong.health.product.model.entity.category.BaseStandardEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * huangpeng
 * 2023/7/7 11:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
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
     * 套餐活动状态:0-待开始，1-进行中, 2-已结束
     */
    private int activityStatus;

    /**
     * 活动套餐价格，单位：分
     */
    private Long price;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     * 活动套餐备注
     */
    private String activityPackageRemark;

    /**
     * 套餐商品列表
     */
    private String activityPackageSkuName;
}
