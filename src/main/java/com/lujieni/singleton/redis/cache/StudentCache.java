package com.lujieni.singleton.redis.cache;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import com.lujieni.singleton.redis.provider.StudentRedisCacheProvider;
import com.lujieni.singleton.redis.redis.DefaultTTLRedisCache;
import com.lujieni.singleton.redis.redis.storage.RedisCacheStorage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentCache extends DefaultTTLRedisCache<StudentPO> implements InitializingBean {

    @Autowired
    private RedisCacheStorage redisCacheStorage;

    @Autowired
    private StudentRedisCacheProvider studentRedisCacheProvider;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setCacheStorage(redisCacheStorage);
        super.setCacheProvider(studentRedisCacheProvider);
    }

}