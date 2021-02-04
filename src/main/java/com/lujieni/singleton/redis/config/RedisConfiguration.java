package com.lujieni.singleton.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lujieni.singleton.redis.config.serializer.FastJsonRedisSerializer;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisStore;

@Configuration
public class RedisConfiguration {

    /**
     *
     * 1.
     *  方法名一定要叫 redisTemplate,否则 springboot仍旧会帮你生成 RedisTemplate
     *  详见RedisAutoConfiguration
     *
     * 2.
     *  redisTemplate 默认序列化使用的jdkSerializeable, 存储二进制字节码导致我们
     *  使用redis客户端工具看的时候只能看到2进制,所以自定义序列化类
     *
     *
     * @param redisConnectionFactory
     *
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer();

       /*
            fastJsonRedisSerializer和jackson2JsonRedisSerializer等价,这里使用一个即可
            Jackson2JsonRedisSerializer <Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        */

        /* 设置string的序列化方式 */
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);

        /* 设置hash的序列化方式 */
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);


        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public RedisCacheStorage redisCacheStorage(RedisTemplate redisTemplate){
        RedisCacheStorage redisCacheStorage = new RedisCacheStorage();
        redisCacheStorage.setRedisTemplate(redisTemplate);

        return redisCacheStorage;
    }



}