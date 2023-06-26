package com.drstrong.health.product.model.request.incentive;

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
 * @date 2023/6/7 14:49
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存收益单元的入参")
public class SaveEarningNameRequest extends OperatorUserInfo implements Serializable {
	private static final long serialVersionUID = 1343456187173950377L;

	@ApiModelProperty("店铺id")
	@NotNull(message = "店铺id不能为空")
	private Long storeId;

	@ApiModelProperty("配置目标类型,和sku的类型字段一致 0-商品，1-药品，2-中药,3-协定方")
	@NotNull(message = "配置目标类型不能为空")
	private Integer configGoalType;

	@ApiModelProperty("收益单元,同店铺同类型下不能重复")
	@NotBlank(message = "收益单元不能为空")
	@Length(max = 20, message = "收益单元长度不能超过20字符")
	private String earningName;
}
