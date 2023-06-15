package com.drstrong.health.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liuqiuyi
 * @date 2023/6/14 16:48
 */
@AllArgsConstructor
@Getter
public enum ScheduledStatusEnum {
	UN_COMPLETE(0, "待处理"),
	SUCCESS(1, "已处理"),
	CANCEL(2, "已取消"),
	IN_PROCESS(3, "处理中"),
	;

	private final Integer code;

	private final String value;
}
