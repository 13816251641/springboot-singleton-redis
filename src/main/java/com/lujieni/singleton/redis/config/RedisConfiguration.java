package com.lujieni.singleton.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lujieni.singleton.redis.config.serializer.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    /**
     * 自定义RedisTemplate的bean，不使用默认的RedisTemplate
     * redisTemplate 默认序列化使用的jdkSerializeable, 存储二进制字节码, 所以自定义序列化类
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        FastJsonRedisSerializer serializer = new FastJsonRedisSerializer();

        Jackson2JsonRedisSerializer <Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);


        redisTemplate.setKeySerializer(new StringRedisSerializer());
        /* 使用基于jackson的序列化方式,该序列化方式可以在redis中保存类的包名+类名,但你直接用String存那就抱歉了 */
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        //redisTemplate.setValueSerializer(serializer);使用基于fastjson的序列化方式

        /* 设置hash的序列化方式  */
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());


        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}