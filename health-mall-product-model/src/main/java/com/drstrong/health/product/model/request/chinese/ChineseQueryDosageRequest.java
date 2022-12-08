package com.drstrong.health.product.model.request.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 中药查询
 *
 * @author liuqiuyi
 * @date 2022/12/8 16:48
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("查询店铺中药有倍数限制的入参")
public class ChineseQueryDosageRequest implements Serializable {
	private static final long serialVersionUID = -3349213419559643001L;

	@ApiModelProperty("店铺 id")
	private Long storeId;

	@ApiModelProperty("互联网医院 id")
	private Long agencyId;

	@ApiModelProperty(value = "用户 id", hidden = true)
	private Long ucUserId;
}
