package com.drstrong.health.product.model.enums;

/**
 * 商品类型
 *
 * @author liuqiuyi
 * @date 2021/12/16 14:04
 */
public enum ProductTypeEnum {
	/**
	 *
	 */
	PRODUCT(0, "商品","P"),
	MEDICINE(1, "药品","M"),
	CHINESE(2, "中药","Z"),
	;

	private Integer code;
	private String value;
	private String mark;

	ProductTypeEnum(Integer code, String value, String mark) {
		this.code = code;
		this.value = value;
		this.mark = mark;
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

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
}
