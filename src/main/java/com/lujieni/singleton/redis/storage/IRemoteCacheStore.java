package com.lujieni.singleton.redis.storage;

public interface IRemoteCacheStore<K,V> {

    boolean set(K var1, V var2);

    V get(K var1);

    void remove(K var1);

    void removeMulti(K... var1);

}