package com.drstrong.health.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum DosageFormEnum {
    COMMON_DRUG(0, "配方颗粒"),
    SPECIFIC_DRUG(1, "饮片");

    private Integer code;

    private String value;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static DosageFormEnum getValueByCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (DosageFormEnum dosageFormEnum : DosageFormEnum.values()) {
            if (code.equals(dosageFormEnum.getCode())) {
                return dosageFormEnum;
            }
        }
        return null;
    }
}
