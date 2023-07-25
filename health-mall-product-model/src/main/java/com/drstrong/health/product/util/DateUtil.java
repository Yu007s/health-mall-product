package com.drstrong.health.product.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * 功能概述：<br>
 * 时间处理工具类
 *
 * @author guoyongxiang
 */
public class DateUtil {

	/**
	 * 判断参数是否等于null或者空
	 *
	 * @param param
	 * @return
	 */
	public static boolean checkParam(Object param) {
		return null == param || "".equals(param);
	}

	/**
	 * 输入日期，按照指定格式返回
	 *
	 * @param date
	 * @param pattern e.g.DATE_FORMAT_8 = "yyyyMMdd"; DATE_TIME_FORMAT_14 =
	 *                "yyyyMMddHHmmss"; 或者类似于二者的格式 <br>
	 *                e.g."yyyyMMddHH"，"yyyyMM"
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (checkParam(pattern) || checkParam(date)) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(date);
	}

	/**
	 * 获取当前时间戳(精确到毫秒)
	 *
	 * @return
	 */
	public static Long getCurrentTime() {
		LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
		Date date = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());
		return date.getTime();
	}

}
