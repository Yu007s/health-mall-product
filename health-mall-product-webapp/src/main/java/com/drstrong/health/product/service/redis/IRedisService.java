package com.drstrong.health.product.service.redis;

import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;

/**
 * @description: redis
 * @Author: JiaoYuSheng
 * @Date: 2021-12-13 15:08
 * @program health-mall-postsale
 */
public interface IRedisService {

	/**
	 * 范围查找List
	 *
	 * @param key
	 * @param ttl      过期时间，单位秒
	 * @param start
	 * @param end      -1代表最后一个
	 * @param supplier 查找不到处理方法
	 * @param <T>
	 * @return
	 */
	<T extends Serializable> List<T> lrange(String key, int ttl, int start, int end, Supplier<List<T>> supplier);

	/**
	 * 根据 key 进行自增
	 *
	 * @author liuqiuyi
	 * @date 2021/12/16 14:43
	 */
	long incr(String key);
}
