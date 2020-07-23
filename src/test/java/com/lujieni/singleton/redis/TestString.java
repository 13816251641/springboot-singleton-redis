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
    /*
     * spring中一旦类指定了泛型的话,装配的类的泛型必须要完全一致
     * 没有指定任何泛型的话匹配本类或者子类的任意泛型或非泛型类
     */
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
        System.out.println(o.toString());
    }

}
