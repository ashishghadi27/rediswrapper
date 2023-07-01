package com.root.redis.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.root.redis.context.RedisSessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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

    private String objectToJson(Object object){
        String value = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            value = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

    private <T> T jsonToObject(String json, Class<T> contextClass){
        T value = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            value = mapper.readValue(json, contextClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return value;
    }

}
