package com.drstrong.health.product.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * pms_category表的 status 状态枚举
 *
 * @author liuqiuyi
 * @date 2023/6/19 14:43
 */
@Getter
@AllArgsConstructor
public enum CategoryStatusEnum {
    /**
     *
     */
    ENABLE(1,"启用"),
    DISABLE(0,"禁用"),
    DELETE(-1,"删除"),;

    private final Integer code;

    private final String value;
}
