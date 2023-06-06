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
 * 			12: 配送费
 * 			21:导入导出
 * </p>
 *
 * @author liuqiuyi
 * @date 2021/12/10 17:28
 */
public enum ErrorEnums implements IBaseResult {
	/**
	 *
	 */
	QUERY_SIZE_TOO_BIG("100001", "查询参数过大"),
	ADD_LOCK_ERROR("100002", "加锁失败!"),
	REMOTE_INVOKE_ERROR("100003", "远程接口调用失败,请联系管理员!"),
	PARAM_IS_NOT_NULL("11000", "参数不能为空"),
	CATEGORY_NOT_EXIST("11100", "分类不存在"),
	SAVE_UPDATE_NOT_EXIST("11101", "更新或者保存的数据不存在"),
	PRODUCT_NOT_EXIST("11102", "商品数据不存在或已下架"),
	CATEGORY_NAME_IS_EXIST("11103", "同级分类中,分类名称已经存在"),
	UPDATE_NOT_ALLOW("11104", "已上架的商品不允许修改"),
	CATEGORY_DELETED_ERROR("11105", "分类下存在商品,不允许删除"),
	PRICE_IS_ERROR("11106", "商品金额必须大于 1"),
	CATEGORY_LEVEL_IS_ERROR("11107", "商品必须关联最小子分类"),
	CHINESE_IS_REPEAT("11108", "同一店铺下相同药材不能重复添加"),
	SKU_IS_NULL("11109", "sku不存在或已下架"),
	CHINESE_MEDICINE_IS_NULL("11110", "中药材不存在"),
	SUPPLIER_IS_NULL("11111", "供应商不存在或未建立关联"),
	SAVE_IS_NULL("11112", "保存数据失败"),
	SKU_NAME_IS_REPEAT("11113", "相同店铺下 sku 名称不能重复"),
	MEDICINE_CODE_NOT_ASSOCIATED("11114", "药材未关联供应商"),
	PARAM_TYPE_IS_ERROR("11115", "参数不符合要求"),

	STORE_NAME_LENGTH("17000", "店铺名称长度不正确"),
	STORE_NAME_REPEAT("17100", "店铺名称已存在"),
	STORE_NOT_EXIST("17110", "店铺不存在"),
	STORE_NOT_RELEVANCE("17111", "商品需要关联sku"),
	STORE_NOT_SETPRICE("17112", "上架商品需要设置进货价"),
	STORE_NOT_SETPOSTAGE("17211", "sku商品未设置邮费，请设置邮费后再上架"),

	EXCEL_EXPORT_ERROR("21000", "文档导出错误"),
	EXCEL_IMPORT_ERROR("21100", "文档导入错误"),

	STORE_POSTAGE_IS_NULL("12001", "配送费信息不存在"),
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
