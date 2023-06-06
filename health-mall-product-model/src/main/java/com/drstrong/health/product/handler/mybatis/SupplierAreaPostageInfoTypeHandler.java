package com.drstrong.health.product.handler.mybatis;

import com.drstrong.health.product.model.entity.postage.StorePostageEntity;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/6 11:58
 */
public class SupplierAreaPostageInfoTypeHandler extends JacksonToListTypeHandler<StorePostageEntity.SupplierAreaPostageInfo> {
	@Override
	protected TypeReference<List<StorePostageEntity.SupplierAreaPostageInfo>> specificType() {
		return new TypeReference<List<StorePostageEntity.SupplierAreaPostageInfo>>() {
		};
	}
}
