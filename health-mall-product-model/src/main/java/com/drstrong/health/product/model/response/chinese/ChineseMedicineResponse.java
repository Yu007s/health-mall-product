package com.drstrong.health.product.model.response.chinese;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/03/9:39
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("一条中药材信息")
public class ChineseMedicineResponse implements Serializable {
    private static final long serialVersionUID = 1515648173694532564L;

    @ApiModelProperty("中药材id")
    private Long medicineId;

    @ApiModelProperty("中药材编码")
    private String medicineCode;

    @ApiModelProperty("药材名称")
    private String name;

    @ApiModelProperty("药材别名列表")
    private String aliNames;

    @ApiModelProperty("最大剂量")
    private BigDecimal maxDosage;

    @ApiModelProperty("中药的相反药材信息")
    private List<ChineseConflictMedicine> chineseConflictMedicineList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ApiModel("中药材的相反药材信息")
    public static class ChineseConflictMedicine implements Serializable {
        private static final long serialVersionUID = -862427787412636601L;

        @ApiModelProperty("中药材编码")
        private String medicineCode;

        @ApiModelProperty("药材名称")
        private String name;
    }
}
