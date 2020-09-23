package com.lujieni.singleton.redis.storage;

public interface IRemoteCacheStore<K,V> {

    /**
     * 永久设值
     * @param var1
     * @param var2
     * @return
     */
    boolean set(K var1, V var2);

    /**
     * 设定过期时间
     * @param var1
     * @param var2
     * @param var3
     * @return
     */
    boolean set(K var1, V var2, int var3);

    V get(K var1);

    /**
     * 从redis中删除数据
     * @param var1
     */
    void remove(K var1);

    /**
     * 从redis中批量删除数据
     * @param var1
     */
    void removeMulti(K... var1);

}