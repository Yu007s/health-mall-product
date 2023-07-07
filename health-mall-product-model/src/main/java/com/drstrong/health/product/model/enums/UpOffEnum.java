package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 上下架的枚举
 *
 * @author liuqiuyi
 * @date 2021/12/16 10:10
 */
public enum UpOffEnum {
	/**
	 *
	 */
	DOWN(0, "已下架"),
	UP(1, "已上架"),
	SCHEDULED_UP(2, "预约上架中"),
	SCHEDULED_DOWN(3, "预约下架中");

	private Integer code;

	private String value;

	public static UpOffEnum getEnumByCode(Integer code) {
		if (Objects.isNull(code)) {
			return null;
		}
		for (UpOffEnum upOffEnum : UpOffEnum.values()) {
			if (Objects.equals(upOffEnum.getCode(), code)) {
				return upOffEnum;
			}
		}
		return null;
	}

	public static String getValueByCode(Integer code) {
		for (UpOffEnum upOffEnum : UpOffEnum.values()) {
			if (Objects.equals(upOffEnum.getCode(), code)) {
				return upOffEnum.getValue();
			}
		}
		return null;
	}

	UpOffEnum(Integer code, String value) {
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
