package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 剂量类型枚举类
 *
 * @author liuqiuyi
 * @date 2022/12/8 15:16
 */
public enum DosageTypeEnum {
	/**
	 *
	 */
	NONE(0, "无限制"),
	MULTIPLE(1, "按倍数限制"),
	;

	private Integer code;

	private String value;

	public static Boolean checkCodeIsExist(Integer code) {
		return Objects.nonNull(getValueByCode(code));
	}

	public static String getValueByCode(Integer code) {
		for (DosageTypeEnum dosageTypeEnum : DosageTypeEnum.values()) {
			if (Objects.equals(code, dosageTypeEnum.code)) {
				return dosageTypeEnum.value;
			}
		}
		return null;
	}

	DosageTypeEnum(Integer code, String value) {
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
