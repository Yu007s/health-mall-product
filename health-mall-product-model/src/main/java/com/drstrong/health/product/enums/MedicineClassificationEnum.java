package com.drstrong.health.product.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * huangpeng
 * 2023/7/20 19:36
 */
@AllArgsConstructor
@NoArgsConstructor
public enum MedicineClassificationEnum {

    DRUG_CLASS_IFICATIONID("drugClassificationId"),
    AGENT_CLASS_IFICATIONID("agentClassificationId"),
    SECURITY_CLASS_IFICATIONID("securityClassificationId"),
    MATERIALS_CLASS_IFICATIONID("materialsClassificationId"),
    PHARMACOLOGY_CLASS_IFICATIONID("pharmacologyClassificationId");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
