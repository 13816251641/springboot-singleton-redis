package com.lujieni.singleton.redis.redis;


import com.lujieni.singleton.redis.provider.ITTLCacheProvider;
import com.lujieni.singleton.redis.redis.exception.KeyIsNotFoundException;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 基于redis的缓存实现
 * @param <V>
 */
public class DefaultTTLRedisCache<V> implements ICache<String,V>, InitializingBean {

    /**
     * 日志类
     */
    private static final Log LOG = LogFactory.getLog(DefaultTTLRedisCache.class);

    /**
     * 数据提供者
     */
    private ITTLCacheProvider<V> cacheProvider;

    /**
     * 数据存储器
     */
    private RedisCacheStorage<String,V> cacheStorage;

    /**
     * 超时时间,单位秒,默认10分钟
     */
    protected int timeOut = 600;


    public void setCacheProvider(ITTLCacheProvider<V> cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    /**
     * RedisCacheStorage cacheStorage 非泛型变量
     * 非泛型变量可以赋值给泛型变量,但有风险,比如这里可以 setCacheStorage(cacheStorage)
     * @param cacheStorage
     */
    public void setCacheStorage(RedisCacheStorage<String, V> cacheStorage) {
        this.cacheStorage = cacheStorage;
    }

    /**
     * 设置超时时间
     * @param seconds
     */
    public void setTimeOut(int seconds) {
        this.timeOut = seconds;
    }


    /**
     * 根据uuid和key生成key
     * @param key
     * @return
     * @see
     */
    protected String getKey(String key) {
        return this.getUUID() + "_" + key;
    }

    /**
     * 没有任何实现,希望子类实现
     * @return
     */
    @Override
    public String getUUID() {
        return "";
    }

    @Override
    public V get(String key){
        if(StringUtils.isEmpty(key)){
            LOG.warn("缓存[" + this.getUUID() + "]，key为空串，返回结果[null]");
            return null;
        }else{
            V value = null;
            try {
                /* 从redis中获取,redis允许存null进去,所以value有可能为空 */
                value = this.cacheStorage.get(this.getKey(key));
                if (value == null) {
                    /* value为null我们也不去查,防止缓存击穿  */
                    value = this.cacheProvider.get(key);
                    LOG.warn("缓存[" + this.getUUID() + "]，key[" + key + "]过期，重新走数据库查询，返回结果[" + value + "]");
                    this.cacheStorage.set(this.getKey(key), null, this.timeOut);
                }
            }catch (KeyIsNotFoundException var1) {
                value = this.cacheProvider.get(key);//从数据库中获取
                LOG.warn("缓存[" + this.getUUID() + "]，key[" + key + "]不存在，走数据库查询，返回结果[" + value + "]");
                this.cacheStorage.set(this.getKey(key), value, this.timeOut);//value有可能为空
            } catch (RedisConnectionFailureException var2) {
                /*
                    redis出现了异常,你把数据再存在redis没有意义
                 */
                value = this.cacheProvider.get(key);
                LOG.warn("redis连接出现异常，走数据库查询!");
                LOG.error(var2);
                return value;
            } catch (Exception var3) {
                LOG.error(var3);
                return value;
            }
            return value;
        }
    }

    @Override
    public void invalid() {
        throw new RuntimeException(this.getUUID() + ":TTLCache cannot invalid all!");
    }

    @Override
    public void invalid(String key) {
        this.cacheStorage.remove(this.getKey(key));
    }

    @Override
    public Map<String, V> get() {
        throw new RuntimeException(this.getUUID() + ":TTLCache cannot get all!");
    }

    @Override
    public void invalidMulti(String... keys) {
        if (keys != null) {
            String[] skeys = new String[keys.length];

            for(int i = 0; i < keys.length; ++i) {
                skeys[i] = keys[i];
            }

            this.cacheStorage.removeMulti(skeys);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}