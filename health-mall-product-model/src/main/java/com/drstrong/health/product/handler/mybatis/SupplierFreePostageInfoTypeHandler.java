package com.drstrong.health.product.handler.mybatis;

import com.drstrong.health.product.model.entity.postage.StorePostageEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/6 11:58
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public class SupplierFreePostageInfoTypeHandler extends JacksonToListTypeHandler<StorePostageEntity.SupplierFreePostageInfo> {
	@Override
	protected TypeReference<List<StorePostageEntity.SupplierFreePostageInfo>> specificType() {
		return new TypeReference<List<StorePostageEntity.SupplierFreePostageInfo>>() {
		};
	}
}
