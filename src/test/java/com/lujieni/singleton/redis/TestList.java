package com.lujieni.singleton.redis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.lujieni.singleton.redis.domain.Person;
import com.lujieni.singleton.redis.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestList {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 将Person存储在list中
     * @throws JsonProcessingException
     */
    @Test
    public void test01() throws JsonProcessingException {
        redisTemplate.opsForList().leftPush("persons", JacksonUtil.toJson(new Person().setName("张三").setAge(28)));
        redisTemplate.opsForList().leftPush("persons", JacksonUtil.toJson(new Person().setName("大哥").setAge(58)));
    }

    /**
     * 从list中读取Person数据(弹出左边第一个)
     * @throws IOException
     */
    @Test
    public void test02() throws IOException {
        Person person = JacksonUtil.readValue((String)redisTemplate.opsForList().leftPop("persons"), Person.class);
        System.out.println(person);
    }

    /**
     * 慎用index,时间复杂度为o(n)
     * @throws IOException
     */
    @Test
    public void test03() throws IOException {
        Person person = JacksonUtil.readValue((String)redisTemplate.opsForList().index("persons", 0),Person.class);
        System.out.println(person);
    }

    /**
     * 批量插入
     * 数码宝贝-0
     * 宝可梦-1
     * @throws Exception
     */
    @Test
    public void test04() throws Exception{
        redisTemplate.opsForList().leftPushAll("persons",JacksonUtil.toJson(new Person().setName("宝可梦").setAge(28)),JacksonUtil.toJson(new Person().setName("数码宝贝").setAge(58)));
    }

    /**
     * 替换对应下标的值,下标有误会数组越界,没有性能问题
     * @throws Exception
     */
    @Test
    public void test05() throws Exception{
        redisTemplate.opsForList().set("persons",0,"a");
    }


}