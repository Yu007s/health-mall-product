package com.drstrong.health.product.model.request.medicine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2023/8/3 16:31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("code查询的参数")
public class MedicineCodeRequest implements Serializable {
    private static final long serialVersionUID = 592108431626788241L;

    @NotNull(message = "商品类型不能为空")
    @ApiModelProperty("商品类型")
    private Integer productType;

    @ApiModelProperty("药材编码")
    private String medicineCode;
}
