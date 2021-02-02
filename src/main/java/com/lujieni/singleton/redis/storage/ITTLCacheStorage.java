package com.lujieni.singleton.redis.storage;

/**
 * redis,ecache任何缓存方式都可以实现这个接口
 * 缓存提供者需要实现的接口
 * @param <K>
 * @param <V>
 */
public interface ITTLCacheStorage<K,V> {

    /**
     * 主动向Cache更新指定的数据
     *
     * @param key
     * @param value
     * @return boolean 是否执行成功
     */
    boolean set(K key, V value);

    /**
     * 主动向Cache更新指定的数据,指定过期时间
     *
     * @param key
     * @param value
     * @param exp
     * @return boolean 是否执行成功
     */
    boolean set(K key, V value, int exp);

    /**
     * 获取缓存
     *
     * @param key 缓存Key
     * @return 缓存Value
     */
    V get(K key);

    /**
     * 删除指定的缓存信息
     *
     * @param key 缓存Key
     */
    void remove(K key);

    /**
     * 删除多个key的缓存信息
     *
     * @param keys 动态参数 数组[]
     * @see
     */
    void removeMulti(K... keys);

}