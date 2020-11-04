package com.lujieni.singleton.redis.provider;

public interface ITTLCacheProvider<V> {

    V get(String key);
}
