package com.drstrong.health.product.model.response.old;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 原有的老接口中,会有一些代码使用的是这个返回值对象,后续这种将逐步废弃
 *
 * @author liuqiuyi
 * @date 2023/6/12 09:46
 */
@Deprecated
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseVO<T> {
	/**
	 * 状态码
	 * 0：表示正常
	 * 其余数值表示出现错误，该值对应ExceptionCode中定义的异常码
	 */
	@ApiModelProperty("状态码，0表示正常")
	private int code;

	/**
	 * 信息内容
	 */
	@ApiModelProperty("错误信息")
	private String msg;

	/**
	 * 数据信息
	 */
	@ApiModelProperty("数据信息")
	private List<T> data;
}
