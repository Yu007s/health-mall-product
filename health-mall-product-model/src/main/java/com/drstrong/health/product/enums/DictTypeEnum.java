package com.drstrong.health.product.enums;

import java.util.Objects;

/**
 * huangpeng
 * 2023/6/9 11:09
 */
public enum DictTypeEnum {

    DOSAGEE_CYCLE_UNITS_DICT("DOSAGEE_CYCLE_UNITS_DICT", "剂量周期单位"),
    DOSE_UNITS_DICT("DOSE_UNITS_DICT", "剂量单位"),
    USAGE_TIMES_DICT("USAGE_TIMES_DICT", "服用时间"),
    USAGE_METHODS_DICT("USAGE_METHODS_DICT", "服用方式"),
    FREQUENCY_OF_USE_DICT("FREQUENCY_OF_USE_DICT", "使用频次");

    private String code;

    private String value;

    public static DictTypeEnum getValueByCode(String code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (DictTypeEnum dictTypeEnum : DictTypeEnum.values()) {
            if (code.equals(dictTypeEnum.getCode())) {
                return dictTypeEnum;
            }
        }
        return null;
    }

    DictTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
