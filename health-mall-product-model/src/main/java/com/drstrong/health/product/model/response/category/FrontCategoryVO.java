package com.drstrong.health.product.model.response.category;

import com.drstrong.health.product.model.BaseTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 前台分类的返回值
 *
 * @author liuqiuyi
 * @date 2021/12/6 09:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("前台分类的返回值")
public class FrontCategoryVO extends BaseTree implements Serializable {
	private static final long serialVersionUID = -4324918492956801359L;

	@ApiModelProperty("前台层级")
	private Integer level;

	@ApiModelProperty("前台层级名称")
	private String levelName;

	@ApiModelProperty("分类名称")
	private String categoryName;

	@ApiModelProperty("商品数量")
	private Integer productCount;

	@ApiModelProperty("icon 图片地址")
	private String icon;

	@ApiModelProperty("排序字段")
	private Integer sort;

	@ApiModelProperty("分类状态 0-启用,1-禁用")
	private Integer categoryStatus;

	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;
}