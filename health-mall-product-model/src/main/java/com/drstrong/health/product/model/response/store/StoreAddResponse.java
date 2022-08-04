package com.drstrong.health.product.model.response.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/07/26/10:12
 */
@Data
@ApiModel("店铺新增页面请求返回信息")
public class StoreAddResponse implements Serializable {
    private static final long serialVersionUID = -221334848674106232L;
    @NotNull
    @ApiModelProperty("店铺类型")
    private List<String> storeTypeNames;

    @ApiModelProperty("互联网医院的名字集合")
    private List<AgencyIdAndName> agencyIdAndNames;

    @ApiModelProperty("供应商集合")
    private List<SupplierResponse> suppliers;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgencyIdAndName implements Serializable {
        private static final long serialVersionUID = -1265517096428941234L;
        Long id;
        String name;
    }

}
