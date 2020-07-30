package com.lujieni.singleton.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSet{

    @Autowired
    private RedisTemplate redisTemplate;

    private ApplicationContext applicationContext;

    /**
     * set 批量插入功能 set无序不可重复
     * @throws JsonProcessingException
     */
    @Test
    public void test01() throws JsonProcessingException {
        redisTemplate.opsForSet().add("books","python","python","java","golang");
    }

    /**
     * set 遍历
     */
    @Test
    public void test02() {
        Set<String> books = redisTemplate.opsForSet().members("books");
        for (String str : books) {
            System.out.println(str);
        }
    }

    /**
     * set 某个value是否存在
     * @throws JsonProcessingException
     */
    @Test
    public void test03() throws JsonProcessingException {
        Boolean result = redisTemplate.opsForSet().isMember("books", "h");
        System.out.println(result);
    }

    /**
     * set 移除并返回集合中的一个随机元素
     * @throws JsonProcessingException
     */
    @Test
    public void test04() throws JsonProcessingException {
        Object result = redisTemplate.opsForSet().pop("books");
        System.out.println(result);
    }

    /**
     * set 取2个set的交集
     * @throws JsonProcessingException
     */
    @Test
    public void test05() throws JsonProcessingException {
        Set<String> set = redisTemplate.opsForSet().intersect("books", "sciences");
        for (String str:set) {
            System.out.println(str);
        }
    }

    /**
     * set 取2个set的并集
     * @throws JsonProcessingException
     */
    @Test
    public void test06() throws JsonProcessingException {
        Set<String> set = redisTemplate.opsForSet().union("books", "sciences");
        for (String str:set) {
            System.out.println(str);
        }
    }

    /**
     * 求a-b的差集,就是a中减去和b之间的交集的那部分,结果和前后顺序相关!!!
     * @throws JsonProcessingException
     */
    @Test
    public void test07() throws JsonProcessingException {
        Set<String> set = redisTemplate.opsForSet().difference("books","sciences");
        for (String str:set) {
            System.out.println(str);
        }
    }



}