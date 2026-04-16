package com.mindskip.wdd.configuration.spring.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @version 1.7.0
 * @description: redis缓存配置
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Bean
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Hash的key也采用StringRedisSerializer的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
        RedisCacheWriter cacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        // 创建一个FastJson2JsonRedisSerializer
        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<>(Object.class);

        // 创建默认的缓存配置
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        // 使用自定义的缓存配置初始化一个RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter, defaultCacheConfig);

        return cacheManager;
    }

    /**
     * spring redis 默认生成key方式,包含::号
     * prefix + ":" + key
     *
     * @param prefix 前缀
     * @param key    配置键
     * @return 结果
     */
    public String simpleKeyGenerator(String prefix, String key) {
        return CacheKeyPrefix.simple().compute(prefix) + key;
    }
}
