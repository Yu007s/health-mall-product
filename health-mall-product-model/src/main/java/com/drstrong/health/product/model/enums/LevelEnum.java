package com.drstrong.health.product.model.enums;

import java.util.Objects;

/**
 * 类目枚举
 *
 * @author liuqiuyi
 * @date 2021/12/9 20:03
 */
public enum LevelEnum {
	/**
	 *
	 */
	ONE(1, "一级分类"),
	TWO(2, "二级分类"),
	THREE(3, "三级分类"),
	;

	private Integer code;

	private String value;

	LevelEnum(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	/**
	 * 根据 code 获取 value
	 *
	 * @author liuqiuyi
	 * @date 2021/12/9 20:06
	 */
	public static String getValueByCode(Integer code) {
		if (Objects.isNull(code)) {
			return "";
		}
		for (LevelEnum levelEnum : LevelEnum.values()) {
			if (Objects.equals(levelEnum.getCode(), code)) {
				return levelEnum.getValue();
			}
		}
		return "";
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
