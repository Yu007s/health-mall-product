package com.drstrong.health.product.model.enums;

/**
 * 来源枚举类
 * <p>可用于区分调用来源,便于相同代码的特殊处理</>
 *
 * @author liuqiuyi
 * @date 2021/12/22 20:07
 */
public enum SourceEnum {
	/**
	 * 远程调用
	 */
	RPC,
	/**
	 * 小程序
	 */
	APPLETS,
	/**
	 * CMS 后台
	 */
	CMS,
	;
}
