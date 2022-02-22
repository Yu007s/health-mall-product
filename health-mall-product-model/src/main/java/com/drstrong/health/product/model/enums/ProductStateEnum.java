package com.drstrong.health.product.model.enums;

/**
 * 删除状态的枚举
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:36
 */
public enum ProductStateEnum {
	/**
	 *
	 */
	HAS_PUT(1, "已上架"),
	UN_PUT(0, "未上架"),
	;

	private Integer code;

	private String value;

	ProductStateEnum(Integer code, String value) {
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
