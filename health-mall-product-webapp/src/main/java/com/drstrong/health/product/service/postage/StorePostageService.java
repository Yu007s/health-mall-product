package com.drstrong.health.product.service.postage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.drstrong.health.product.model.entity.postage.StorePostageEntity;
import com.drstrong.health.product.model.request.store.SaveStoreSupplierPostageRequest;

public interface StorePostageService extends IService<StorePostageEntity> {
	/**
	 * 根据店铺 id 查询配送费信息
	 *
	 * @author liuqiuyi
	 * @date 2023/6/6 14:08
	 */
	StorePostageEntity queryByStoreId(Long storeId);

	/**
	 * 保存或者更新店铺供应商的全局配送费信息
	 *
	 * @param saveStoreSupplierPostageRequest 入参
	 * @author liuqiuyi
	 * @date 2023/6/6 15:02
	 */
	void saveOrUpdateSupplierPostageInfo(SaveStoreSupplierPostageRequest saveStoreSupplierPostageRequest);
}
