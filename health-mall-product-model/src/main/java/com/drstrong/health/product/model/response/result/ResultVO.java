package com.drstrong.health.product.model.response.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 之前的统一响应实体
 *
 * @author liuqiuyi
 * @date 2021/12/9 11:41
 */
@ApiModel(
		description = "统一响应实体"
)
@JsonIgnoreProperties(
		ignoreUnknown = true
)
public class ResultVO<T> implements Serializable {
	private static final long serialVersionUID = -2186263261028826002L;
	@ApiModelProperty("0表示操作成功，否则表示操作失败")
	private String code = "0";
	@ApiModelProperty("操作成功或失败后的提示信息")
	private String msg;
	@ApiModelProperty("数据内容")
	private T data;
	private Throwable error;
	private boolean success;
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss"
	)
	private Date timestamp = new Date();

	public ResultVO(String code, String msg, T data, boolean success) {
		this.code = code;
		this.msg = msg;
		this.data = data;
		this.success = success;
	}

	public ResultVO() {
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public T getData() {
		return this.data;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public static <T> ResultVO<T> success(IBaseResult resultStatus) {
		return of(null, resultStatus.getCode(), resultStatus.getMessage(), true);
	}

	public static <T> ResultVO<T> success(String msg) {
		return of(null, ResultStatus.SUCCESS.getCode(), msg, true);
	}

	public static <T> ResultVO<T> success(T model, String msg) {
		return of(model, ResultStatus.SUCCESS.getCode(), msg, true);
	}

	public static <T> ResultVO<T> success(T model) {
		return of(model, ResultStatus.SUCCESS.getCode(), "", true);
	}

	public static <T> ResultVO<T> success() {
		return of(null, ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), true);
	}

	public static <T> ResultVO<T> failed(String msg) {
		return of(null, ResultStatus.ERROR.getCode(), msg, false);
	}

	public static <T> ResultVO<T> failed(IBaseResult resultStatus) {
		return of(null, resultStatus.getCode(), resultStatus.getMessage(), false);
	}

	public static <T> ResultVO<T> failed(BusinessException ex) {
		return of(null, ex.getCode(), ex.getMessage(), false);
	}

	public static <T> ResultVO<T> failed(T model, String msg) {
		return of(model, ResultStatus.ERROR.getCode(), msg, false);
	}

	public static <T> ResultVO<T> failed(T model) {
		return of(model, ResultStatus.ERROR.getCode(), "", false);
	}

	public static <T> ResultVO<T> of(T datas, String code, String msg, boolean success) {
		return new ResultVO(code, msg, datas, success);
	}

	public ResultVO<T> code(String code) {
		this.code = code;
		return this;
	}

	public ResultVO<T> message(String msg) {
		this.msg = msg;
		return this;
	}

	public ResultVO<T> data(T data) {
		this.data = data;
		return this;
	}

	public ResultVO<T> type(ResultStatus resultStatus) {
		this.code = resultStatus.getCode();
		this.msg = resultStatus.getMessage();
		return this;
	}

	public ResultVO<T> error(Throwable error) {
		this.error = error;
		return this;
	}

	public boolean isSuccess() {
		return this.success;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("code", this.code).add("msg", this.msg).add("data", this.data).add("error", this.error).add("timestamp", this.timestamp).toString();
	}

	public Map<String, Object> toModel() {
		Map<String, Object> result = new HashMap<>(8);
		result.put("code", this.code);
		result.put("msg", this.msg);
		result.put("data", this.data);
		result.put("error", this.error);
		result.put("timestamp", this.timestamp);
		return result;
	}
}
