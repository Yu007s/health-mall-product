package com.drstrong.health.product.service.impl;

import cn.strong.common.utils.CollectionUtils;
import cn.strong.springboot.redis.utils.CacheLettuceUtils;

import com.drstrong.health.product.service.IRedisService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    @Override
    public <T extends Serializable> List<T> lrange(String key, int ttl, int start, int end, Supplier<List<T>> supplier){
        List<T> result = CacheLettuceUtils.lrange(key, start, end);
        if(CollectionUtils.isNotEmpty(result)){
            return result;
        }
        result = supplier.get();
        if(CollectionUtils.isNotEmpty(result)){
            result.forEach(i -> CacheLettuceUtils.rpush(key, i));
            if(ttl > 0){
                CacheLettuceUtils.expire(key, ttl);
            }
        }
        return result;
    }
}
