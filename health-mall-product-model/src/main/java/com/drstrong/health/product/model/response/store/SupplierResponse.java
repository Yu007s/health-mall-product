package com.drstrong.health.product.model.response.store;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xieYueFeng
 * @Date 2022/08/02/15:30
 */
@Data
public class SupplierResponse implements Serializable {

    private static Long serialVersionUID = 1518489121731961744L;
    /**
     * 供应商id
     */
    Long supplierId;
    /**
     * 供应商名字
     */
    String supplierName;
}
