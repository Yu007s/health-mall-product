package com.drstrong.health.product.model.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/6/10 15:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("分类 DTO")
public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = -7597209252034225711L;

	@ApiModelProperty("分类 id")
	private Long categoryId;

	@ApiModelProperty("分类名称")
	private String categoryName;
}
