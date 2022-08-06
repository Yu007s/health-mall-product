package com.drstrong.health.product.model.response.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/05/13:39
 */
@Data
@ApiModel("省份信息返回值  嵌套市")
public class ProvinceAreaInfo implements Serializable {

    private static final long serialVersionUID = -1564484874945554894L;
    @ApiModelProperty("省级地区 id")
    private Long areaId;

    @ApiModelProperty("省级地区名称")
    private String areaName;

    @ApiModelProperty("省级下面所有市")
    private List<AreaInfoResponse> cities;

}
