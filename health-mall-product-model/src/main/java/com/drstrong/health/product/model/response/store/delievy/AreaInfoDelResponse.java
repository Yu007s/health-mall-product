package com.drstrong.health.product.model.response.store.delievy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/10:11
 */
@Data
public class AreaInfoDelResponse {
    private static final long serialVersionUID = -1568485241564168748L;

    @ApiModelProperty("区域id")
    private Long areaId;

    @ApiModelProperty("区域类型")
    private Integer areaType;

    @ApiModelProperty("地区名称")
    private String areaName;
}
