package com.drstrong.health.product.handler.mybatis;

import com.drstrong.health.product.model.entity.incentive.SkuIncentivePolicyEntity;
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
public class IncentivePolicyInfoTypeHandler extends JacksonToListTypeHandler<SkuIncentivePolicyEntity.IncentivePolicyInfo> {
	@Override
	protected TypeReference<List<SkuIncentivePolicyEntity.IncentivePolicyInfo>> specificType() {
		return new TypeReference<List<SkuIncentivePolicyEntity.IncentivePolicyInfo>>() {
		};
	}
}
