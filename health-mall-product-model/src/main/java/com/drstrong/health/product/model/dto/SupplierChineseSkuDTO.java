package com.drstrong.health.product.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuqiuyi
 * @date 2022/8/5 10:21
 */
@Data
@ApiModel("供应商的中药材信息接收对象")
public class SupplierChineseSkuDTO implements Serializable {
	private static final long serialVersionUID = -3856054584411311676L;

	/**
	 * sku 编码
	 */
	private String skuCode;

	/**
	 * sku 名称
	 */
	private String skuName;

	/**
	 * 老的中药材表主键id，兼容老数据需要
	 */
	private Long oldMedicineId;

	/**
	 * 药材库code
	 */
	private String medicineCode;

	/**
	 * 店铺id
	 */
	private Long storeId;

	/**
	 * 价格/克,单位:分
	 */
	private Long price;

	/**
	 * sku上下架状态；0-未上架，1-已上架
	 */
	private Integer skuStatus;

	/**
	 * 供应商 id
	 */
	private Long supplierId;
}
