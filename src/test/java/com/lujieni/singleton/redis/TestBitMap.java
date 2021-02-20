package com.lujieni.singleton.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Package: com.lujieni.singleton.redis
 * @ClassName: TestBitMap
 * @Author: lujieni
 * @Description: 1
 * @Date: 2021-02-20 16:32
 * @Version: 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestBitMap {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test01(){
        redisTemplate.opsForValue().setBit("bit",20,true);
    }

    @Test
    public void test02(){
        Boolean bit = redisTemplate.opsForValue().getBit("bit", 12);
        System.out.println(bit);
    }

    @Test
    public void test03(){
        Object result = redisTemplate.execute((RedisCallback<Long>) (redisConnection -> {
                    return redisConnection.bitCount("bit".getBytes());
                })
        );
        System.out.println(result);
    }

}