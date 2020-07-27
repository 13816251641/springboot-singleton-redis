package com.lujieni.singleton.redis.redis;


import com.lujieni.singleton.redis.provider.ITTLCacheProvider;
import com.lujieni.singleton.redis.redis.exception.KeyIsNotFoundException;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.util.StringUtils;

public class DefaultTTLRedisCache<V>{

    private RedisCacheStorage<String,V> cacheStorage;

    protected ITTLCacheProvider<V> cacheProvider;

    private static final Log LOG = LogFactory.getLog(DefaultTTLRedisCache.class);

    public void setCacheStorage(RedisCacheStorage<String, V> cacheStorage) {
        this.cacheStorage = cacheStorage;
    }

    public void setCacheProvider(ITTLCacheProvider<V> cacheProvider) {
        this.cacheProvider = cacheProvider;
    }

    public V get(String key){
        if(StringUtils.isEmpty(key)){
            return null;
        }else{
            V value = null;
            try {
                value = this.cacheStorage.get(key);
            } catch (KeyIsNotFoundException var1) {
                value = this.cacheProvider.get(key);
                LOG.warn("缓存key[" + key + "]不存在，走数据库查询，返回结果[" + value + "]");
                if (value != null) {
                    this.cacheStorage.set(key, value);
                }
            } catch (RedisConnectionFailureException var2) {
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

    public void invalid(String key) {
        this.cacheStorage.remove(key);
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