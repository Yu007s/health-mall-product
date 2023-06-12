package com.drstrong.health.product.model.response.medicine;


import com.drstrong.health.product.model.response.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;



/**
 * <p>
 * 西药简要信息VO
 * </p>
 *
 * @author zzw
 * @since 2023-06-12
 */

@Data
@Builder
@ApiModel(description = "西药简要信息VO")
public class WesternMedicineSimpleInfoVO {

    @ApiModelProperty(value = "西药id")
    private Long id;

    @ApiModelProperty(value = "药品编码")
    private String medicineCode;

    @ApiModelProperty(value = "药品名称")
    private String medicineName;

    @ApiModelProperty(value = "剂型分类名称")
    private String agentClassName;

    @ApiModelProperty(value = "规格列表")
    private PageVO<WesternMedicineSpecVO> specPageList;
}
