package com.lujieni.singleton.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lujieni.singleton.redis.domain.Person;
import com.lujieni.singleton.redis.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestString {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 测试将对象以json形式存入redis中的string
     * 如果在存的时候将对象转为json形式的字符串了,那么很可惜redis中无法存储对象的包名+类名,从redis中得到的就是String类型的
     * 如果在存的时候没有变化,那么从redis中得到的对象是带有类型的,类型就是你当初存的类型
     */
    @Test
    public void testSaveObj2Redis() throws Exception {
       Person person = new Person("陆哥哥520", 27);
       redisTemplate.opsForValue().set("com.lujieni2", person);
    }

    @Test
    public void testGetDataFromRedis() throws Exception {
        Object o = redisTemplate.opsForValue().get("com.lujieni2");
        System.out.println(o.getClass().getName());//com.lujieni.singleton.redis.domain.Person
    }

    /**
     * 可以存null进redis !!!
     * @throws Exception
     */
    @Test
    public void testSetNullToRedis() throws Exception {
        redisTemplate.opsForValue().set("null",null);
        //redisTemplate.opsForValue().set("null","a");

    }

    /**
     * 从redis中取出的值可以为null
     * @throws Exception
     */
    @Test
    public void testGetNullToRedis() throws Exception {
        Object value = redisTemplate.opsForValue().get("null");
        System.out.println(value == null);
    }

}
