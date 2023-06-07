package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 药品基础分类枚举
 *
 * @author zzw
 * @date 2023/06/06 17:08
 */
public enum MedicineClassificationEnum {

    PHARMACOLOGICAL_CLASSIFICATION(1, "药理分类"),
    TYPE_AGENT_CLASSIFICATION(2, "型剂分类"),
    DRUG_CLASSIFICATION(3, "药品分类"),
    SECURITY_CLASSIFICATION(4, "安全分类"),
    RAW_MATERIAL_CLASSIFICATION(5, "原料分类");

    private Integer code;
    private String value;

    public static String getValueByCode(Integer code) {
        for (MedicineClassificationEnum attributeEnum : MedicineClassificationEnum.values()) {
            if (Objects.equals(attributeEnum.getCode(), code)) {
                return attributeEnum.getValue();
            }
        }
        return "";
    }

    MedicineClassificationEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

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
}
