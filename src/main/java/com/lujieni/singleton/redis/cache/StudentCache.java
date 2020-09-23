package com.lujieni.singleton.redis.cache;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import com.lujieni.singleton.redis.provider.StudentCacheProvider;
import com.lujieni.singleton.redis.redis.DefaultTTLRedisCache;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class StudentCache extends DefaultTTLRedisCache<StudentPO> {

    public static final String UUID = StudentCache.class.getName();

    @Autowired
    private RedisCacheStorage redisCacheStorage;//从redis中取

    @Autowired
    private StudentCacheProvider studentCacheProvider;//从数据库中取

    @Override
    public void afterPropertiesSet() {
        super.setCacheStorage(redisCacheStorage);
        super.setCacheProvider(studentCacheProvider);
        setTimeOut(5*60);//5分钟
    }

    @Override
    public String getUUID() {
        return UUID;
    }

}