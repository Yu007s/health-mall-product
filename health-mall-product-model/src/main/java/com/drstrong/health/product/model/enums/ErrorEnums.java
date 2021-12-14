package com.drstrong.health.product.model.enums;

import com.drstrong.health.product.model.response.result.IBaseResult;

/**
 * 商品项目错误码定义
 * <p>
 * 错误码和错误信息定义类
 * 		1. 错误码定义规则为5位数字
 * 		2. 前两位表示业务场景，最后三位表示错误码。
 * 			如：10001，10表示通用，001表示系统未知异常
 * 		3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
 * 			错误码列表：
 * 			10：通用
 * 			001：参数格式校验失败
 * 			11：商品
 * 			12：订单
 * 			13：购物车
 * 			14：物流
 * 			15：优惠券
 * 			16：用户
 * 			17:店铺
 * 			20：库存
 * </p>
 *
 * @author liuqiuyi
 * @date 2021/12/10 17:28
 */
public enum ErrorEnums implements IBaseResult {
	/**
	 *
	 */
	PARAM_IS_NOT_NULL("11000","参数不能为空"),
	CATEGORY_NOT_EXIST("11100","分类不存在"),
	SAVE_UPDATE_NOT_EXIST("11101","更新或者保存的数据不存在"),
	PRODUCT_NOT_EXIST("11102","商品数据不存在"),
	CATEGORY_NAME_IS_EXIST("11103","同级分类中,分类名称已经存在"),
	STORE_NAME_LENGTH("17000","店铺名称长度不正确"),
	STORE_NAME_REPEAT("17100","店铺名称已存在"),
	STORE_NOT_EXIST("17110","店铺不存在"),
	;

	private String code;

	private String message;

	ErrorEnums(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
