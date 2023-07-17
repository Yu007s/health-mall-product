package com.drstrong.health.product.model.request.sku.recommend;

import com.drstrong.health.product.model.dto.product.SkuBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author liuqiuyi
 * @date 2023/7/10 16:27
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("保存或者新增sku的推荐信息")
public class SaveRecommendRequest extends SkuBaseDTO implements Serializable {
    private static final long serialVersionUID = -6187083928122352783L;

    @ApiModelProperty("关键字列表")
    @NotNull(message = "关键字列表不能为空")
    private Set<String> keywordList;
}
