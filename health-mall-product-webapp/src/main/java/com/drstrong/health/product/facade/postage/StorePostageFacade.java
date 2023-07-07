package com.drstrong.health.product.facade.postage;

import com.drstrong.health.product.model.request.store.SaveStorePostageRequest;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;
import com.drstrong.health.product.model.response.store.v3.StorePostageVO;

/**
 * 店铺配送费
 *
 * @author liuqiuyi
 * @date 2023/6/6 13:58
 */
public interface StorePostageFacade {
	/**
	 * 保存店铺包邮金额,并记录日志
	 *
	 * @author liuqiuyi
	 * @date 2023/6/6 14:03
	 */
	void saveStorePostage(SaveStorePostageRequest saveStorePostageRequest);

	/**
	 * 保存店铺下供应商的邮费信息,并记录日志
	 *
	 * @author liuqiuyi
	 * @date 2023/6/6 14:49
	 */
	void saveStoreSupplierPostage(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest);

	/**
	 * 查询店铺的配送费信息和店铺下所有供应商的配送费信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/6 15:55
	 */
	StorePostageVO queryStorePostage(Long storeId);
}
