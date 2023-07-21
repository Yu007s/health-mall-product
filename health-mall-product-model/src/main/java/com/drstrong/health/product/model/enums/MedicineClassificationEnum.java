package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 药品基础分类枚举
 *
 * @author zzw
 * @date 2023/06/06 17:08
 */
public enum MedicineClassificationEnum {

    PHARMACOLOGICAL_CLASSIFICATION(1, "药理分类", "pharmacologyClassificationId"),
    TYPE_AGENT_CLASSIFICATION(2, "型剂分类", "agentClassificationId"),
    DRUG_CLASSIFICATION(3, "药品分类", "drugClassificationId"),
    SECURITY_CLASSIFICATION(4, "安全分类", "securityClassificationId"),
    RAW_MATERIAL_CLASSIFICATION(5, "原料分类", "materialsClassificationId");

    private Integer code;
    private String value;
    private String name;

    public static String getValueByCode(Integer code) {
        for (MedicineClassificationEnum attributeEnum : MedicineClassificationEnum.values()) {
            if (Objects.equals(attributeEnum.getCode(), code)) {
                return attributeEnum.getValue();
            }
        }
        return "";
    }

    MedicineClassificationEnum(Integer code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
