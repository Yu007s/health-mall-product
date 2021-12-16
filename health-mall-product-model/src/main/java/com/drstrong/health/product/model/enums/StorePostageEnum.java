package com.drstrong.health.product.model.enums;

/**
 * 邮费设置状态
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:36
 */
public enum StorePostageEnum {
	/**
	 *
	 */
	HAS_SET(1, "已设置"),
	UN_SET(0, "未设置"),
	;

	private Integer code;

	private String value;

	StorePostageEnum(Integer code, String value) {
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
