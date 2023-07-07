package com.drstrong.health.product.model.response.medicine;


import com.baomidou.mybatisplus.annotation.TableField;
import com.drstrong.health.product.model.response.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 西药简要信息VO
 * </p>
 *
 * @author zzw
 * @since 2023-06-12
 */

@Data
@ApiModel(description = "西药简要信息VO")
public class WesternMedicineSimpleInfoVO implements Serializable {

    private static final long serialVersionUID = -6243348318106155159L;
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

    public WesternMedicineSimpleInfoVO(Long id, String medicineCode, String medicineName, String agentClassName) {
        this.id = id;
        this.medicineCode = medicineCode;
        this.medicineName = medicineName;
        this.agentClassName = agentClassName;
    }
}
