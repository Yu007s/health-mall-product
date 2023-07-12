package com.drstrong.health.product.model.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * huangpeng
 * 2023/7/11 20:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductWesternDetailVO implements Serializable {

    private static final long serialVersionUID = 7891913589559446114L;

    /**
     * 属性name
     */
    @ApiModelProperty(value = "属性name")
    private String attributeNmae;

    /**
     * 属性名称
     */
    @ApiModelProperty(value = "属性名称")
    private String attributeKey;
    /**
     * 属性值
     */
    @ApiModelProperty(value = "属性值")
    private String attributeValue;
}
