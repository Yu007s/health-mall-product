package com.drstrong.health.product.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * huangpeng
 * 2023/7/20 19:24
 */

@AllArgsConstructor
@NoArgsConstructor
public enum MedicineAttributeEnum {

    COMMON_DRUG(1L, "常用药"),
    SPECIFIC_DRUG(2L, "特效药");

    private Long code;

    private String value;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static MedicineAttributeEnum getValueByCode(Long code) {
        if (Objects.isNull(code)) {
            return null;
        }
        for (MedicineAttributeEnum medicineAttributeEnum : MedicineAttributeEnum.values()) {
            if (code.equals(medicineAttributeEnum.getCode())) {
                return medicineAttributeEnum;
            }
        }
        return null;
    }
}
