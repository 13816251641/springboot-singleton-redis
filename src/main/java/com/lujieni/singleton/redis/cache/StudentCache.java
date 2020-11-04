package com.lujieni.singleton.redis.cache;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import com.lujieni.singleton.redis.provider.StudentCacheProvider;
import com.lujieni.singleton.redis.redis.DefaultTTLRedisCache;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StudentCache extends DefaultTTLRedisCache<StudentPO> {

    /**
     * com.lujieni.singleton.redis.cache.StudentCache
     */
    public static final String UUID = StudentCache.class.getName();

    /**
     * 从redis中读取,在RedisConfiguration中已经实例化
     */
    @Autowired
    private RedisCacheStorage redisCacheStorage;

    /**
     * 从数据库中读取
     */
    @Autowired
    private StudentCacheProvider studentCacheProvider;

    @Override
    public void afterPropertiesSet() {
        /*
            非泛型变量可以赋值给泛型变量,但有风险
         */
        super.setCacheStorage(redisCacheStorage);
        super.setCacheProvider(studentCacheProvider);
        setTimeOut(5*60);//5分钟
    }

    @Override
    public String getUUID() {
        return UUID;
    }


}