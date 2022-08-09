package com.drstrong.health.product.model.response.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 省份信息返回值
 *
 * @author lsx
 * @date 2021/12/7 10:00
 */
@Data
@ApiModel("省份信息返回值")
public class AreaInfoResponse implements Serializable {

	private static final long serialVersionUID = -5888708784299486536L;
	@ApiModelProperty("地区 id")
	private Long areaId;

	@ApiModelProperty("地区名称")
	private String areaName;

}
