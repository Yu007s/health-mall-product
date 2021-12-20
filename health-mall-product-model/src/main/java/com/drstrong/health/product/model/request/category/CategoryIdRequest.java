package com.drstrong.health.product.model.request.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分类 id 入参
 *
 * @author liuqiuyi
 * @date 2021/12/6 13:49
 */
@Data
@ApiModel("分类 id 入参")
public class CategoryIdRequest implements Serializable {
	private static final long serialVersionUID = -3424054955441711698L;

	@ApiModelProperty(value = "分类 id", required = true)
	@NotNull(message = "分类 id 不能为空")
	private Long categoryId;

	@NotBlank(message = "userId 不能为空")
	private String userId;
}
