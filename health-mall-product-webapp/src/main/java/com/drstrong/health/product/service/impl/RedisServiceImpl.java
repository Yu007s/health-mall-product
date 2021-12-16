package com.drstrong.health.product.service.impl;

import cn.strong.common.utils.CollectionUtils;
import com.drstrong.health.product.service.IRedisService;
import com.drstrong.health.redis.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @description: redis
 * @Author: JiaoYuSheng
 * @Date: 2021-12-13 15:10
 * @program health-mall-postsale
 */
@Service
public class RedisServiceImpl implements IRedisService {
    private static RedisUtils redisUtils;
    @Autowired
    public RedisServiceImpl(RedisUtils redisUtils) {
        RedisServiceImpl.redisUtils = redisUtils;
    }
    @Override
    public <T extends Serializable> List<T> lrange(String key, int ttl, int start, int end, Supplier<List<T>> supplier){
        List<Object> r = redisUtils.lGet(key, start, end);
        List<T> result =  new ArrayList<>();
        for (Object item : r) {
            result.add((T) item);
        }
        if(CollectionUtils.isNotEmpty(result)){
            return result;
        }
        result = supplier.get();
        if(CollectionUtils.isNotEmpty(result)){
            result.forEach(i -> redisUtils.lSet(key, i));
            if(ttl > 0){
                redisUtils.expire(key, ttl);
            }
        }
        return result;
    }

	/**
	 * 根据 key 进行自增
	 *
	 * @param serialNum
	 * @param l
	 * @author liuqiuyi
	 * @date 2021/12/16 14:43
	 */
	@Override
	public long incr(String key) {
		return redisUtils.incr(key, 1L);
	}
}
