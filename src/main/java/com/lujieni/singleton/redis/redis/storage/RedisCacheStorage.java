package com.lujieni.singleton.redis.redis.storage;

import com.lujieni.singleton.redis.redis.exception.KeyIsNotFoundException;
import com.lujieni.singleton.redis.redis.exception.RedisCacheStorageException;
import com.lujieni.singleton.redis.storage.IRemoteCacheStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 操作redis的封装类
 * @param <K>
 * @param <V>
 */
public class RedisCacheStorage<K,V> implements IRemoteCacheStore<K,V>, InitializingBean {
    /**
     * 日志
     */
    private static final Log log = LogFactory.getLog(RedisCacheStorage.class);

    /**
     * 实际进行缓存操作的template，由spring提供
     */
    private RedisTemplate redisTemplate;

    public RedisCacheStorage() {

    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存入数据，默认时效：10分钟
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(K key, V value) {
        return this.set(key, value, 600);
    }

    /**
     * 存入有时效的数据
     * @param key
     * @param value
     * @param exp
     * @return boolean 是否执行成功
     */
    @Override
    public boolean set(K key, V value, int exp) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        } else {
            this.redisTemplate.opsForValue().set(key, value, (long)exp, TimeUnit.SECONDS);
            return true;
        }
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new RedisCacheStorageException("key does not allow for null!");
        } else {
            boolean exist = this.redisTemplate.hasKey(key);
            if (!exist) {
                throw new KeyIsNotFoundException("key is not found!");
            } else {
                Object obj = this.redisTemplate.opsForValue().get(key);
                return (V) obj;
            }
        }
    }

    /**
     * <p>删除指定的缓存信息</p>
     *
     * @param key
     */
    @Override
    public void remove(K key) {
        this.redisTemplate.delete(key);
    }

    /**
     * <p>删除多个key的缓存信息</p>
     *
     * @param keys
     */
    @Override
    public void removeMulti(K... keys) {
        this.redisTemplate.delete(keys);
    }

    @Override
    public void afterPropertiesSet() {

    }
}