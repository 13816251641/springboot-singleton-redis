package com.lujieni.singleton.redis.provider;

/**
 * 数据提供者如db需要实现的接口
 * @param <V>
 */
public interface ITTLCacheProvider<V> {

    V get(String key);
}
