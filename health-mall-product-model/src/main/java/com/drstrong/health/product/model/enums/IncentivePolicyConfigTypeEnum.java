package com.drstrong.health.product.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 政策的配置目标枚举
 *
 * @author liuqiuyi
 * @date 2023/8/3 17:58
 */
@Getter
@AllArgsConstructor
public enum IncentivePolicyConfigTypeEnum {
    /**
     *
     */
    PRODUCT(0, "商品"),
    MEDICINE(1, "西药"),
    CHINESE(2, "中药"),
    AGREEMENT(3, "协定方(预制)"),
    HEALTH(4, "健康用品"),
    CHINESE_PATENT_DRUG(5, "中成药"),
    SINGLE_PACKAGE(6, "单品套餐");

    private final Integer code;

    private final String value;
}
