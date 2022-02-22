package com.drstrong.health.product.model.enums;

/**
 * 启用禁用的枚举
 *
 * @author lsx
 * @date 2021.12.13
 */
public enum StoreStatusEnum {
	/**
	 *
	 */
	ENABLE(0, "启用"),
	DISABLE(1, "禁用"),
	;

	private Integer code;

	private String value;

	StoreStatusEnum(Integer code, String value) {
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
