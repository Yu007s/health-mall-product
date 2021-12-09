package com.drstrong.health.product.model.response.result;

import java.util.Objects;

/**
 * 业务异常类,参照之前 drcnstrong 的代码
 *
 * @author liuqiuyi
 * @date 2021/12/9 11:39
 */
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 7799116654610030850L;
	private String code;

	public BusinessException(String message) {
		super(message);
		this.code = ResultStatus.FAIL.getCode();
	}

	public BusinessException(Exception e) {
		this(e.getMessage());
	}

	public BusinessException(IBaseResult baseResult) {
		this(baseResult.getCode(), baseResult.getMessage());
	}

	public BusinessException(String code, String message) {
		super(message);
		if (Objects.isNull(code)) {
			code = ResultStatus.ERROR.getCode();
		}

		this.code = code;
	}

	public String getCode() {
		return this.code;
	}
}
