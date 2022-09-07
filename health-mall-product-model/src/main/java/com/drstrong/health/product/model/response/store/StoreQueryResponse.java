package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<AgencyIdAndName> agencyIdAndNames;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgencyIdAndName implements Serializable {
        private static final long serialVersionUID = -1265517096428941234L;
        String id;
        String name;
    }
}
