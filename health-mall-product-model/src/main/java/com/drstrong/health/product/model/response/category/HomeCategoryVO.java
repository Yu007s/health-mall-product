package com.drstrong.health.product.model.response.category;

import com.drstrong.health.product.model.BaseTree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 首页分类展示信息
 *
 * @author liuqiuyi
 * @date 2021/12/15 16:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("首页分类展示信息")
public class HomeCategoryVO extends BaseTree implements Serializable {
	private static final long serialVersionUID = -5548879820099964615L;

	@ApiModelProperty("前台分类名称")
	private String categoryName;

	@ApiModelProperty("分类的 icon")
	private String icon;

	@ApiModelProperty("前台分类层级")
	private Integer level;

	@ApiModelProperty("排序字段")
	private Integer sort;

	public static HomeCategoryVO buildDefault(String categoryName, String icon) {
		HomeCategoryVO categoryVO = new HomeCategoryVO();
		categoryVO.setId(0L);
		categoryVO.setCategoryName(categoryName);
		categoryVO.setIcon(icon);
		categoryVO.setLevel(0);
		return categoryVO;
	}
}
