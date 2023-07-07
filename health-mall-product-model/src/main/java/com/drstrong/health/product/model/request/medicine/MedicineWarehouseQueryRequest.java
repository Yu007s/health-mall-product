package com.drstrong.health.product.model.request.medicine;

import com.drstrong.health.product.model.request.PageRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 药材库查询的入参
 *
 * @author liuqiuyi
 * @date 2023/6/20 10:26
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicineWarehouseQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = -8819282034641428862L;

    @NotNull(message = "商品类型不能为空")
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("搜索key，可能是名称，也可能是code，根据业务自行处理")
    private String searchKey;

}
