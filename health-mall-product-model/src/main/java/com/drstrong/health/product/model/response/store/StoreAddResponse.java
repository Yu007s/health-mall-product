package com.drstrong.health.product.model.response.store;

import com.drstrong.health.product.model.enums.StoreTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    private List<StoreTypeEnum> storeTypeNames;

    @ApiModelProperty("互联网医院的名字集合")
    private List<String> agencyNameList;

}
