package com.drstrong.health.product.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 收益政策的枚举
 *
 * @author liuqiuyi
 * @date 2023/6/9 10:09
 */
@Getter
@AllArgsConstructor
public enum EarningPolicyTypeEnum {
	FIXEDNESS(1, "固定奖励"),
	SCALE(2, "比例提成"),
	NONE(3, "无收益"),
	;

	private final Integer code;

	private final String value;

	public static String getValueByCode(Integer code) {
		return Stream.of(EarningPolicyTypeEnum.values())
				.filter(earningPolicyTypeEnum -> Objects.equals(earningPolicyTypeEnum.getCode(), code))
				.map(EarningPolicyTypeEnum::getValue)
				.findFirst().orElse("");
	}
}
