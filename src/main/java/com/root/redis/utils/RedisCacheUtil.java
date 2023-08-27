package com.root.redis.utils;

import com.root.redis.context.RedisSessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.root.redis.utils.JsonSerializationUtils.jsonToObject;
import static com.root.redis.utils.JsonSerializationUtils.objectToJson;

@Component
public class RedisCacheUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final int DEFAULT_TIMEOUT = 1800;

    private static final int PERMANENT_TIMEOUT = -1;

    public RedisCacheUtil(@Autowired RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public <T extends RedisSessionContext> T getCache(String key, Class<T> contextClass){
        String json = (String) redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotEmpty(json)){
            return jsonToObject(json, contextClass);
        }
        return null;
    }

    public String getCacheString(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }
    public void setCachePermanently(String key, Object object){
        String value = objectToJson(object);
        redisTemplate.opsForValue().set(key, value, PERMANENT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void setCache(String key, Object object){
        String value = objectToJson(object);
        redisTemplate.opsForValue().set(key, value, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void setCache(String key, Object object, int timeout){
        String value = objectToJson(object);
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }


    public void setCacheStringPermanently(String key, String value){
        redisTemplate.opsForValue().set(key, value, PERMANENT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void setCacheString(String key, String value){
        redisTemplate.opsForValue().set(key, value, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    public void setCacheString(String key, String value, int timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void deleteCache(String key){
        redisTemplate.delete(key);
    }

}