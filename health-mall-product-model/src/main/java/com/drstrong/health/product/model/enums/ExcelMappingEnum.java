package com.drstrong.health.product.model.enums;

/**
 * 删除状态的枚举
 *
 * @author liuqiuyi
 * @date 2021/12/9 15:36
 */
public enum ExcelMappingEnum {
	/**
	 *
	 */
	STORE_SKU_EXPORT("storeSkuExport", "店铺商品导出明细","没有可导出的店铺商品"),
	;

	private String mappingId;

	private String excelName;

	private String emptyMessage;

	ExcelMappingEnum(String mappingId, String excelName, String emptyMessage) {
		this.mappingId = mappingId;
		this.excelName = excelName;
		this.emptyMessage = emptyMessage;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

	public String getEmptyMessage() {
		return emptyMessage;
	}

	public void setEmptyMessage(String emptyMessage) {
		this.emptyMessage = emptyMessage;
	}
}
