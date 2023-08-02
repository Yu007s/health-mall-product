package com.drstrong.health.product.model.response.chinese;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xieYueFeng
 * @Date 2022/08/08/9:47
 */
@Data
@ApiModel("中药材信息列表查询返回")
public class ChineseMedicineSearchVO implements Serializable {
    private final static long serialVersionUID = 1578412369433532564L;
    private List<ChineseMedicineResponse> medicineResponses;
    private Long total;
    private Integer pageNo;
    private Integer pageSize;
}
