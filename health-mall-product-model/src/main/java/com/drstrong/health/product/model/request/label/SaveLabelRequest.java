package com.drstrong.health.product.model.request.label;


import com.drstrong.health.product.model.request.OperatorUserInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/5/31 17:17
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("标签 DTO")
public class SaveLabelRequest extends OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = 1961302019690514546L;

	@ApiModelProperty("标签 id")
	private Long id;

	@ApiModelProperty("标签名称")
	@NotBlank(message = "标签名称不能为空")
	@Length(max = 10, message = "最大长度不能超过 10 字符")
	private String labelName;

	@ApiModelProperty("店铺id")
	@NotNull(message = "店铺id不能为空")
	private Long storeId;

	@ApiModelProperty("标签类型,和sku的类型字段一致")
	@NotNull(message = "标签类型不能为空")
	private Integer labelType;
}