package com.drstrong.health.product.model.response.result;

public enum ResultStatus implements IBaseResult {
	/**
	 *
	 */
	UNAUTHORIZED("1000", "未经授权，无法访问"),
	INVALID_TOKEN("1101", "无法解析的Token，也许Token已经失效"),
	INVALID_GRANT("1102", "账号或者密码错误！"),
	INVALID_SCOPE("1103", "授权范围错误"),
	INVALID_CLIENT("1104", "非法的客户端"),
	ACCESS_DENIED("1201", "拒绝访问"),
	ACCESS_DENIED_AUTHORITY_LIMITED("1202", "权限不足，拒绝访问"),
	SUCCESS("0", "成功"),
	SESSION_INVALID("1", "用户信息验证失败，请重新登录"),
	REPEAT_ERROR("2", "请勿重复操作"),
	FAIL("4000", "失败"),
	WARNING("4001", "警告"),
	METHOD_NOT_ALLOWED("4105", "请求方法不支持"),
	PARAM_ERROR("4106", "参数异常"),
	BAD_SQL_GRAMMAR("6000", "低级SQL语法错误，检查SQL能否正常运行或者字段名称是否正确"),
	DATA_INTEGRITY_VIOLATION("6200", "该数据正在被其它数据引用，请先删除引用关系，再进行数据删除操作"),
	PIPELINE_INVALID_COMMANDS("7100", "Redis管道包含一个或多个无效命令"),
	INVALID_REQUEST("2002", "Invalid Request"),
	REDIRECT_URI_MISMATCH("2005", "Redirect Uri Mismatch"),
	UNAUTHORIZED_CLIENT("2006", "Unauthorized Client"),
	EXPIRED_TOKEN("2007", "Expired Token"),
	UNSUPPORTED_GRANT_TYPE("2008", "Unsupported Grant Type"),
	UNSUPPORTED_RESPONSE_TYPE("2009", "Unsupported Response Type"),
	UNSUPPORTED_MEDIA_TYPE("2010", "Unsupported Media Type"),
	SIGNATURE_DENIED("2013", "Signature Denied"),
	ACCESS_DENIED_BLACK_IP_LIMITED("4031", "Access Denied Black Ip Limited"),
	ACCESS_DENIED_WHITE_IP_LIMITED("4032", "Access Denied White Ip Limited"),
	ACCESS_DENIED_UPDATING("4034", "Access Denied Updating"),
	ACCESS_DENIED_DISABLED("4035", "Access Denied Disabled"),
	ACCESS_DENIED_NOT_OPEN("4036", "Access Denied Not Open"),
	BAD_CREDENTIALS("3000", "Bad Credentials"),
	ACCOUNT_DISABLED("3001", "Account Disabled"),
	ACCOUNT_EXPIRED("3002", "Account Expired"),
	CREDENTIALS_EXPIRED("3003", "Credentials Expired"),
	ACCOUNT_LOCKED("3004", "Account Locked"),
	USERNAME_NOT_FOUND("3005", "Username Not Found"),
	USER_IS_DISABLED("3006", "User is disabled"),
	BAD_REQUEST("4100", "Bad Request"),
	NOT_FOUND("4104", "Not Found"),
	MEDIA_TYPE_NOT_ACCEPTABLE("4106", "Media Type Not Acceptable"),
	TOO_MANY_REQUESTS("4129", "Too Many Requests"),
	ERROR("5220", "Error"),
	GATEWAY_TIMEOUT("5004", "Gateway Timeout"),
	SERVICE_UNAVAILABLE("5003", "Service Unavailable");

	private final String code;
	private final String message;

	private ResultStatus(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ResultStatus getResultEnumByCode(String code) {
		ResultStatus[] var1 = values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			ResultStatus type = var1[var3];
			if (type.getCode().equals(code)) {
				return type;
			}
		}

		return ERROR;
	}

	public static ResultStatus getResultEnumByMessage(String message) {
		ResultStatus[] var1 = values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			ResultStatus type = var1[var3];
			if (type.getMessage().equals(message)) {
				return type;
			}
		}

		return ERROR;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public static boolean isFailed(IBaseResult resultStatus) {
		return !isSuccess(resultStatus);
	}

	public static boolean isSuccess(IBaseResult resultStatus) {
		return SUCCESS.code.equalsIgnoreCase(resultStatus.getCode());
	}
}
