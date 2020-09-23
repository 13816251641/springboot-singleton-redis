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

public abstract class DefaultTTLRedisCache<V> implements ICache<String,V>, InitializingBean {
    private static final Log LOG = LogFactory.getLog(DefaultTTLRedisCache.class);

    private ITTLCacheProvider<V> cacheProvider;//缓存提供接口,对应不同表的provider

    private RedisCacheStorage<String,V> cacheStorage;//操作redis的封装类

    protected int timeOut = 600;

    public void setCacheProvider(ITTLCacheProvider<V> cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public void setCacheStorage(RedisCacheStorage<String, V> cacheStorage) {
        this.cacheStorage = cacheStorage;
    }

    public void setTimeOut(int seconds) {
        this.timeOut = seconds;
    }

    protected String getKey(String key) {
        return this.getUUID() + "_" + key;
    }

    public V get(String key){
        if(StringUtils.isEmpty(key)){
            LOG.warn("缓存[" + this.getUUID() + "]，key为空串，返回结果[null]");
            return null;
        }else{
            V value = null;
            try {
                /* 从redis中获取,value有可能为空 */
                value = this.cacheStorage.get(this.getKey(key));
                if (value == null) {
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

    public void invalid() {
        throw new RuntimeException(this.getUUID() + ":TTLCache cannot invalid all!");
    }

    public void invalid(String key) {
        this.cacheStorage.remove(this.getKey(key));
    }

    public Map<String, V> get() {
        throw new RuntimeException(this.getUUID() + ":TTLCache cannot get all!");
    }

    public void invalidMulti(String... keys) {
        if (keys != null) {
            String[] skeys = new String[keys.length];

            for(int i = 0; i < keys.length; ++i) {
                skeys[i] = keys[i];
            }

            this.cacheStorage.removeMulti(skeys);
        }
    }


}