package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 商品属性枚举
 *
 * @author liuqiuyi
 * @date 2021/12/16 17:40
 */
public enum CommAttributeEnum {
	/**
	 * 1-常用药
	 * 2-新特药I类
	 * 3-新特药II类
	 */
	COMMONLY_USED(1, "常用药"),
	NEW_ONE(2, "新特药I类"),
	NEW_TWO(3, "新特药II类"),
	;

	private Integer code;
	private String value;

	public static String getValueByCode(Integer code) {
		for (CommAttributeEnum attributeEnum : CommAttributeEnum.values()) {
			if (Objects.equals(attributeEnum.getCode(), code)) {
				return attributeEnum.getValue();
			}

		}
		return "";
	}

	CommAttributeEnum(Integer code, String value) {
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
