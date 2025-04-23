package com.sim.video.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

//redis 캐시 관련 객체 설정을 담고 있는 컨피그래이션
@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) { //RedisConnectionFactory를 주입받아 RedisCacheManager생성
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() //Redis 캐시 설정 불러옴
                .entryTtl(Duration.ofMinutes(10)) //TTL설정(10분)
                //캐시 값을 json형태로 직렬화하면서,GenericJackson2JsonRedisSerializer를 사용하면 모든 객체를 json형태로 변환함
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                        )
                );
        //bean으로 반환
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
