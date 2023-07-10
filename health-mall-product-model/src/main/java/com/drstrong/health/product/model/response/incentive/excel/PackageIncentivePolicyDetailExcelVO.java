package com.drstrong.health.product.model.response.incentive.excel;

import com.drstrong.health.product.model.response.incentive.PackageIncentivePolicyDetailVO;
import com.drstrong.health.product.model.response.incentive.SkuIncentivePolicyDetailVO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * huangpeng
 * 2023/7/10 14:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel("套餐政策信息导出 响应值")
public class PackageIncentivePolicyDetailExcelVO implements Serializable {

    private static final long serialVersionUID = -1187340892314571982L;

    @ApiModelProperty("店铺下的政策配置项")
    private Map<Long, Map<Long, String>> storePolicyConfigIdsMap;

    @ApiModelProperty("Package的政策信息")
    private List<PackageIncentivePolicyDetailVO> PackageIncentivePolicyDetailVOList;
}
