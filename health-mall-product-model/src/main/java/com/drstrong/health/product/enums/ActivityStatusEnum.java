package com.drstrong.health.product.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Objects;

/**
 * @author huangpeng
 * @date 2023/7/18 16:48
 */
public enum ActivityStatusEnum {

	TO_BE_STARTED(0, "待开始"),
	UNDER_WAY(1, "进行中"),
	ALREADY_ENDED(2, "已结束");

	private Integer code;

	private String value;

	ActivityStatusEnum(Integer code, String value) {
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

	public static ActivityStatusEnum getValueByCode(Integer code) {
		if (Objects.isNull(code)) {
			return null;
		}
		for (ActivityStatusEnum activitytatusEnum : ActivityStatusEnum.values()) {
			if (code.equals(activitytatusEnum.getCode())) {
				return activitytatusEnum;
			}
		}
		return null;
	}

}
