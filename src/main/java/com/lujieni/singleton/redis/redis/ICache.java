package com.lujieni.singleton.redis.redis;

import java.util.Map;

public interface ICache<K,V> {

    String getUUID();

    V get(K var1);

    Map<K, V> get();

    void invalid();

    void invalid(K var1);

    void invalidMulti(K... var1);


}
