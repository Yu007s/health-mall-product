package com.drstrong.health.product.model.response.cms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author liuqiuyi
 * @date 2021/12/22 16:56
 */
@Data
public class DictVO implements java.io.Serializable {
	private static final long serialVersionUID = 379963436836338904L;
	/**
	 * 字典类型
	 */
	@JsonIgnore
	private String type;
	/**
	 * 字典码
	 */
	private String code;
	/**
	 * 字典值
	 */
	private String value;

	private String url;
}