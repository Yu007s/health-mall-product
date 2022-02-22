package com.drstrong.health.product.listener;

import com.drstrong.health.product.SpringBootTests;
import com.drstrong.health.product.util.RedisKeyUtils;
import com.drstrong.health.redis.utils.RedisUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author liuqiuyi
 * @date 2022/1/10 15:04
 */
public class StoreChangeMqListenerTest extends SpringBootTests {
	@Resource
	private RedisUtils redisUtils;

	@Test
	public void doStoreChangeMqListenerTest() {
		String lockKey = RedisKeyUtils.getStoreChangeKey(1L);
		boolean lockFlag = redisUtils.setIfAbsent(lockKey, 1L, 60 * 3);
		System.out.println(lockFlag);
		boolean lockFlag_TWO = redisUtils.setIfAbsent(lockKey, 1L, 60 * 3);
		System.out.println(lockFlag_TWO);
	}
}
