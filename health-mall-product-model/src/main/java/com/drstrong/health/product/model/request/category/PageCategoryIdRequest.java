package com.drstrong.health.product.model.request.category;

import com.drstrong.health.product.model.request.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分页查询分类商品的入参
 *
 * @author liuqiuyi
 * @date 2021/12/15 20:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("根据分类查询商品的入参(分页)")
public class PageCategoryIdRequest extends PageRequest implements Serializable {
	private static final long serialVersionUID = 6346193245767057413L;

	@ApiModelProperty(value = "分类 id", required = true)
	@NotNull(message = "categoryId 不能为空")
	private Long categoryId;
}
