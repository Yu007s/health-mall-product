package com.drstrong.health.product.remote.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回数据
 */
@Getter
@Setter
@ToString
public
class DictResponse<T> implements java.io.Serializable {

	private static final long serialVersionUID = -8021701502673639026L;
	private int code;

	private String msg;

	private T data;

	private DictResponse() {
		this.code = 0;
		this.msg = "success";
	}

	private DictResponse(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static DictResponse error() {
		return DictResponse.error("未知异常，请联系管理员");
	}

	public static <T> DictResponse<T> error(String msg) {
		return DictResponse.error(500, msg, null);
	}

	public static <T> DictResponse<T> error(int code, String msg) {
		return DictResponse.error(code, msg, null);
	}

	public static <T> DictResponse<T> error(int code, String msg, T data) {
		return new DictResponse<>(code, msg, data);
	}

	public static <T> DictResponse<T> ok() {
		return DictResponse.ok(null);
	}

	public static <T> DictResponse<T> ok(T data) {
		DictResponse<T> dictResponse = new DictResponse<>();
		dictResponse.setData(data);
		return dictResponse;
	}

}
