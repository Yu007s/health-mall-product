package com.drstrong.health.product.model.response.store;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/15:30
 */
@Data
public class SupplierResponse implements Serializable {

    private static long serialVersionUID = 1518489121731961744L;
    /**
     * 供应商id
     */
    private Long supplierId;
    /**
     * 供应商名字
     */
    private String supplierName;
}
