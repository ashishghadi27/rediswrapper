package com.root.redis.services;

import com.root.commondependencies.exception.ValidationException;
import com.root.redis.context.RedisSessionContext;
import com.root.redis.utils.RedisCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.root.redis.constants.ExceptionConstants.INVALID_CACHING_KEY;
import static com.root.redis.constants.ExceptionConstants.INVALID_CONTEXT;

@Component
public class RedisContextWrapper {

    private final RedisCacheUtil redisCacheUtil;

    public RedisContextWrapper(@Autowired RedisCacheUtil redisCacheUtil){
        this.redisCacheUtil = redisCacheUtil;
    }

    public void setContext(String sessionId, RedisSessionContext redisSessionContext) throws ValidationException {
        if(StringUtils.isEmpty(sessionId)
                || StringUtils.isEmpty(redisSessionContext.getContextIdentifier())){
            throw new ValidationException.Builder().errorMessage(INVALID_CONTEXT).build();
        }
        String key = sessionId + redisSessionContext.getContextIdentifier();
        if(redisSessionContext.sessionExpiryTime() != null){
            redisCacheUtil.setCache(key, redisSessionContext, redisSessionContext.sessionExpiryTime());
        }
        else {
            redisCacheUtil.setCache(key, redisSessionContext);
        }
    }

    public void setContextWithNoTimeout(String sessionId, RedisSessionContext redisSessionContext) throws ValidationException {
        if(StringUtils.isEmpty(sessionId)
                || StringUtils.isEmpty(redisSessionContext.getContextIdentifier())){
            throw new ValidationException.Builder().errorMessage(INVALID_CONTEXT).build();
        }
        String key = sessionId + redisSessionContext.getContextIdentifier();
        redisCacheUtil.setCachePermanently(key, redisSessionContext);
    }

    public <T extends RedisSessionContext> T getContext(String sessionId, Class<T> contextClass) throws ValidationException {
        String contextKey = null;
        try {
            contextKey = StringUtils.isNotEmpty(contextClass.newInstance().getContextIdentifier())
                    ? contextClass.newInstance().getContextIdentifier() : "";
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        if(StringUtils.isEmpty(sessionId)
                || StringUtils.isEmpty(contextKey)){
            throw new ValidationException.Builder().errorMessage(INVALID_CONTEXT).build();
        }
        String key = sessionId + contextKey;
        return redisCacheUtil.getCache(key, contextClass);
    }

    public void cacheJsonString(String key, String value) throws ValidationException {
        if(StringUtils.isEmpty(key)){
            throw new ValidationException.Builder().errorMessage(INVALID_CACHING_KEY).build();
        }
        redisCacheUtil.setCacheString(key, value);
    }

    public void cacheJsonString(String key, String value, int timeout) throws ValidationException {
        if(StringUtils.isEmpty(key)){
            throw new ValidationException.Builder().errorMessage(INVALID_CACHING_KEY).build();
        }
        redisCacheUtil.setCacheString(key, value, timeout);
    }

    public void cacheJsonStringWithNoTimeout(String key, String value) throws ValidationException {
        if(StringUtils.isEmpty(key)){
            throw new ValidationException.Builder().errorMessage(INVALID_CACHING_KEY).build();
        }
        redisCacheUtil.setCacheStringPermanently(key, value);
    }
    public String getCachedJson(String key) throws ValidationException {
        if(StringUtils.isEmpty(key)){
            throw new ValidationException.Builder().errorMessage(INVALID_CACHING_KEY).build();
        }
        return redisCacheUtil.getCacheString(key);
    }

    public <T extends RedisSessionContext> T deleteCache(String sessionId, Class<T> contextClass){
        String contextKey = null;
        try {
            contextKey = StringUtils.isNotEmpty(contextClass.newInstance().getContextIdentifier())
                    ? contextClass.newInstance().getContextIdentifier() : "";
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String key = sessionId + contextKey;
        redisCacheUtil.deleteCache(key);
        return null;
    }

}