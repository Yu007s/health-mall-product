package com.drstrong.health.product.model.response.incentive.excel;

import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author liuqiuyi
 * @date 2023/6/13 16:47
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("sku政策信息导出 响应值")
public class SkuIncentivePolicyDetailExcelVO {
	private static final long serialVersionUID = -2287340892314571982L;

	@ApiModelProperty("店铺下的政策配置项")
	private Map<Long, Map<Long, String>> storePolicyConfigIdsMap;

	@ApiModelProperty("sku 的政策信息")
	private List<SkuIncentivePolicyDetailVO> skuIncentivePolicyDetailVOList;
}
