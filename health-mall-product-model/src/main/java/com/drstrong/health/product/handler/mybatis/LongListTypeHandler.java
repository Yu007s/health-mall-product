package com.drstrong.health.product.handler.mybatis;

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
public class LongListTypeHandler extends JacksonToListTypeHandler<Long> {
	@Override
	protected TypeReference<List<Long>> specificType() {
		return new TypeReference<List<Long>>() {
		};
	}
}
