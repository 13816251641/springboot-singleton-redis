package com.lujieni.singleton.redis.config.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 利用fastjson,自己构造序列化对象
 * 比起用StringRedisSerializer序列化方式存,这个序列化方式的好处是可以存储对象的包名+类名在redis中
 */
public class FastJsonRedisSerializer implements RedisSerializer<Object> {
    private static final ParserConfig defaultRedisConfig = new ParserConfig();

    static {
        defaultRedisConfig.setAutoTypeSupport(true);
    }

    public FastJsonRedisSerializer() {
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return new byte[0];
        } else {
            try {
                return JSON.toJSONBytes(object, new SerializerFeature[]{SerializerFeature.WriteClassName});
            } catch (Exception var3) {
                throw new SerializationException("Could not serialize: " + var3.getMessage(), var3);
            }
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes != null && bytes.length != 0) {
            try {
                return JSON.parseObject(new String(bytes, IOUtils.UTF8), Object.class, defaultRedisConfig, new Feature[0]);
            } catch (Exception var3) {
                throw new SerializationException("Could not deserialize: " + var3.getMessage(), var3);
            }
        } else {
            return null;
        }
    }
}