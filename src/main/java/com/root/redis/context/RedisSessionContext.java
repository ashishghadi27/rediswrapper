package com.root.redis.context;

import lombok.Data;

@Data
public abstract class RedisSessionContext {

    private String contextIdentifier;
    public abstract String getContextIdentifier();

    public abstract Integer sessionExpiryTime();
}
