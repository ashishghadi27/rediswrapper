package com.root.redis.config;

import com.root.redis.vo.ConnectionDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

@Configuration
public class RedisConfig {

    private final RedisConsulConfig redisConsulConfig;

    public RedisConfig(@Autowired RedisConsulConfig redisConsulConfig){
        this.redisConsulConfig = redisConsulConfig;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = null;
        if(redisConsulConfig.hasMultipleClusters()){
            List<ConnectionDataVO> connectionDataList = redisConsulConfig.getRedisConnectionHostPort();
            List<String> clusters = connectionDataList.stream()
                    .map(x -> (x.getHost() + ":" + x.getPort())).toList();
            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusters);
            jedisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
        }
        else {
            ConnectionDataVO connectionData = redisConsulConfig.getRedisConnectionHostPort()
                    .stream().findFirst().orElse(new ConnectionDataVO());
            RedisStandaloneConfiguration redisStandaloneConfiguration
                    = new RedisStandaloneConfiguration(connectionData.getHost(),
                    Integer.parseInt(connectionData.getPort()));
            jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        }
        //redisStandaloneConfiguration.setPassword(RedisPassword.of("password"));
        jedisConnectionFactory.setPoolConfig(poolConfig());
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // other settings...
        return template;
    }

    @Bean
    public JedisPoolConfig poolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        return jedisPoolConfig;
    }

}
