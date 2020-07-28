package com.lujieni.singleton.redis.redis.storage;

import com.lujieni.singleton.redis.redis.exception.KeyIsNotFoundException;
import com.lujieni.singleton.redis.storage.IRemoteCacheStore;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisCacheStorage<K,V> implements IRemoteCacheStore<K,V> {

    private RedisTemplate redisTemplate;

    public RedisCacheStorage() {
    }


    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean set(K key, V value) {
        if (key == null) {
            throw new RuntimeException("key does not allow for null!");
        } else {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        }
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new RuntimeException("key does not allow for null!");
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

    @Override
    public void remove(K key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public void removeMulti(K... var1) {
        this.redisTemplate.delete(var1);
    }
}