package com.drstrong.health.product.handler.mybatis;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author liuqiuyi
 * @date 2023/6/6 11:34
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({List.class})
public abstract class JacksonToListTypeHandler<T> extends AbstractJsonTypeHandler<List<T>> {
	private static final Logger log = LoggerFactory.getLogger(JacksonToListTypeHandler.class);
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected List<T> parse(String json) {
		try {
			return objectMapper.readValue(json, specificType());
		} catch (IOException var3) {
			throw new RuntimeException(var3);
		}
	}

	@Override
	protected String toJson(List<T> obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException var3) {
			throw new RuntimeException(var3);
		}
	}

	protected abstract TypeReference<List<T>> specificType();

	public static void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "ObjectMapper should not be null", new Object[0]);
		JacksonToListTypeHandler.objectMapper = objectMapper;
	}
}
