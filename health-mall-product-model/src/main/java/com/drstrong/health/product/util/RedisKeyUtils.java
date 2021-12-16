package com.drstrong.health.product.util;

/**
 * redis key 的工具类
 *
 * @author liuqiuyi
 * @date 2021/12/16 13:56
 */
public class RedisKeyUtils {

	private static final String PREFIX = "naiterui-b2c|";
	private static final String COLON = ":";
	/**
	 * 获取商品唯一编号
	 */
	private static final String SPU_SERIAL_NUMBER = "product_serial_number";

	private static final String SKU_SERIAL_NUMBER = "product_sku_serial_number_";

	/**
	 * 获取商品的唯一编号,自增
	 * <p> 和之前 b2c 中的保持一致 </>
	 *
	 * @author liuqiuyi
	 * @date 2021/12/16 14:01
	 */
	public static String getSerialNum() {
		return PREFIX + SPU_SERIAL_NUMBER;
	}

	/**
	 * 获取 sku 唯一编号,自增
	 * <p> 和之前 b2c 中的保持一致 </>
	 *
	 * @author liuqiuyi
	 * @date 2021/12/16 14:57
	 */
	public static String getSkuNum(Long productId) {
		return PREFIX + SKU_SERIAL_NUMBER + productId;
	}
}
