package com.drstrong.health.product.handler.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.util.List;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public class StringListTypeHandler extends JacksonToListTypeHandler<String> {
	@Override
	protected TypeReference<List<String>> specificType() {
		return new TypeReference<List<String>>() {
		};
	}
}
