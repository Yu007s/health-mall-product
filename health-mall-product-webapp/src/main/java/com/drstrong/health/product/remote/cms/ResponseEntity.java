package com.drstrong.health.product.remote.cms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回数据
 */
@Getter
@Setter
@ToString
class ResponseEntity<T> implements java.io.Serializable {
	private static final long serialVersionUID = 5840782391054671990L;

	private int code;

	private String msg;

	private T data;

	private ResponseEntity() {
		this.code = 0;
		this.msg = "success";
	}

	private ResponseEntity(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static ResponseEntity error() {
		return ResponseEntity.error("未知异常，请联系管理员");
	}

	public static <T> ResponseEntity<T> error(String msg) {
		return ResponseEntity.error(500, msg, null);
	}

	public static <T> ResponseEntity<T> error(int code, String msg) {
		return ResponseEntity.error(code, msg, null);
	}

	public static <T> ResponseEntity<T> error(int code, String msg, T data) {
		return new ResponseEntity<>(code, msg, data);
	}

	public static <T> ResponseEntity<T> ok() {
		return ResponseEntity.ok(null);
	}

	public static <T> ResponseEntity<T> ok(T data) {
		ResponseEntity<T> responseEntity = new ResponseEntity<>();
		responseEntity.setData(data);
		return responseEntity;
	}

}
