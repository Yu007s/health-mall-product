package com.drstrong.health.product.model.response.area;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/09/16:58
 */
@Data
public class StoreAreaInfo  implements Serializable {

    private static final long serialVersionUID = -1235641189447889565L;
    @ApiModelProperty("地区 id")
    private String value;

    @ApiModelProperty("地区名称")
    private String label;
}
