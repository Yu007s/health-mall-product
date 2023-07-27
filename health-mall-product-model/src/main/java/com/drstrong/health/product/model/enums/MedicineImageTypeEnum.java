package com.drstrong.health.product.model.enums;

/**
 * huangpeng
 * 2023/7/27 20:02
 */

public enum MedicineImageTypeEnum {

    TYPE_BIG(1, "大图"),
    TYPE_THUMB(2, "缩略图"),
    TYPE_ICON(3, "icon");

    private Integer code;

    private String value;

    MedicineImageTypeEnum(Integer code, String value) {
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
