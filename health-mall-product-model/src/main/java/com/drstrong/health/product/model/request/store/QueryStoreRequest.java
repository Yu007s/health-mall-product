package com.drstrong.health.product.model.request.store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/7/14 10:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("店铺查询的入参")
public class QueryStoreRequest implements Serializable {
    private static final long serialVersionUID = -8427478124878050191L;

    @ApiModelProperty("店铺名称，支持模糊查询")
    private String storeName;

    @ApiModelProperty("店铺类型： 0 互联网医院 1 其它")
    private Integer storeType;

    @ApiModelProperty("互联网医院id")
    private Set<Long> agencyIds;

    @ApiModelProperty("店铺id")
    private Set<Long> storeIds;
}
