package com.easemob.chattyai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis的配置类
 * @author AnAloneJaver
 * @date 2023/11/25 17:43
 */
@Configuration
public class RedisConfiguration {

    private static final StringRedisSerializer STRING_SERIALIZER = new StringRedisSerializer();

    @Value("${spring.redis.host:}")
    private String host;
    @Value("${spring.redis.password:}")
    private String password;
    @Value("${spring.redis.max-idle:}")
    private Integer maxIdle;
    @Value("${spring.redis.min-idle:}")
    private Integer minIdle;
    //@Value("${spring.redis.maxTotal:}")
    private Integer maxTotal = null;
    @Value("${spring.redis.max-wait:}")
    private Long maxWaitMillis;


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key序列化
        redisTemplate.setKeySerializer(STRING_SERIALIZER);
        // value序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash key序列化
        redisTemplate.setHashKeySerializer(STRING_SERIALIZER);
        // Hash value序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}