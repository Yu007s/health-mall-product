package com.drstrong.health.product.model.enums;

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
	DOWN(0, "未上架"),
	UP(1, "已上架"),
	;

	private Integer code;

	private String value;

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
