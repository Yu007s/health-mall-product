package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/06/12:24
 */
@Data
public class StoreQueryResponse  implements Serializable {
    private static final long serialVersionUID = -1645849851906244565L;
    @NotNull
    @ApiModelProperty("店铺类型名字集合")
    private List<String> storeTypeNames;

    @ApiModelProperty("互联网医院的名字集合")
    private List<StoreAddResponse.AgencyIdAndName> agencyIdAndNames;

}
