package com.drstrong.health.product.model.request.chinese;


import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/09/11:43
 */
@Data
public class ChineseMedicineSearchRequest implements Serializable {
    private final static long serialVersionUID = 1231894214894213213L;
    private String medicineName;
    private String medicineCode;
    private Integer pageNo;
    private Integer pageSize;
}
