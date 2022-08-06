package com.drstrong.health.product.model.request.chinese;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2022/8/5 17:14
 */
@Data
public class StoreDataInitializeRequest implements Serializable {
	private static final long serialVersionUID = -3764959412314402783L;

	@NotNull(message = "供应商 id 不能为空")
	private Long supplierId;

	private List<Long> storeIds;

	private List<CompensateInfo> compensateInfoList;

	@Data
	public static class CompensateInfo {
		private Long storeId;

		private Long medicineId;
	}
}
