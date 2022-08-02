package com.drstrong.health.product.model.response.chinese;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/21:26
 */
@Data
public class ChineseMedicineInfoResponse implements Serializable {

    private static final long serialVersionUID = 523793164462626498L;

    private String medicineCode;
    private String medicineName;
}
