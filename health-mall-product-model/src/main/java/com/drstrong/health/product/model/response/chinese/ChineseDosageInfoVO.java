package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/12/9 15:02
 */
@Data
@ApiModel("中药剂量倍数和单帖最大值的返回对象")
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChineseDosageInfoVO implements Serializable {
	private static final long serialVersionUID = 8998864508279307461L;

	@ApiModelProperty("单帖最小的克数,该值可配置")
	private Integer postsMinGram;

	@ApiModelProperty("存在剂量倍数限制的中药材列表")
	private List<ChineseSkuInfoVO> dosageChineseSkuInfoList;
}
