package com.drstrong.health.product.remote.model;



import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ChineseMedicineDTO implements Serializable {

    private static final long serialVersionUID = -5997425803076497105L;

    private Long id;

    private String medicineName;

    private String medicineCode;

    private String medicinePinyin;

    private String aliasPinyin;

    private String medicineAlias;

    private BigDecimal maxDosage;
}
