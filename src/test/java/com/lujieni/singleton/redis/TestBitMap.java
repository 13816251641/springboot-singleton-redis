package com.lujieni.singleton.redis;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.BitSet;
import java.util.Calendar;
import java.util.List;

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

    /*
        获得签到的总次数
     */
    @Test
    public void test03(){
        Object result = redisTemplate.execute((RedisCallback<Long>) (redisConnection -> {
                    return redisConnection.bitCount("bit".getBytes());
                })
        );
        System.out.println(result);
    }



    @Before
    public void initLogin() {
        /*
         * 初始化登录
         */
        String userId = "911";// 用户id
        String key = userId+"-"+2021+"-"+3;
        redisTemplate.opsForValue().setBit(key, 0, true);
        redisTemplate.opsForValue().setBit(key, 1, true);
        redisTemplate.opsForValue().setBit(key, 2, true);
        redisTemplate.opsForValue().setBit(key, 3, true);
        redisTemplate.opsForValue().setBit(key, 4, true);
        redisTemplate.opsForValue().setBit(key, 5, true);
        redisTemplate.opsForValue().setBit(key, 6, true);
        redisTemplate.opsForValue().setBit(key, 7, true);
        redisTemplate.opsForValue().setBit(key, 8, true);
        redisTemplate.opsForValue().setBit(key, 9, true);
        redisTemplate.opsForValue().setBit(key, 10, true);
        redisTemplate.opsForValue().setBit(key, 11, true);
        redisTemplate.opsForValue().setBit(key, 12, true);
        redisTemplate.opsForValue().setBit(key, 13, true);
        redisTemplate.opsForValue().setBit(key, 14, true);
        redisTemplate.opsForValue().setBit(key, 15, true);// 17号没有登录
        redisTemplate.opsForValue().setBit(key, 17, true);
        redisTemplate.opsForValue().setBit(key, 18, true);
        redisTemplate.opsForValue().setBit(key, 19, true);
        redisTemplate.opsForValue().setBit(key, 20, true);
        redisTemplate.opsForValue().setBit(key, 21, true);
        redisTemplate.opsForValue().setBit(key, 22, true);
        redisTemplate.opsForValue().setBit(key, 23, true);
        redisTemplate.opsForValue().setBit(key, 24, true);
        redisTemplate.opsForValue().setBit(key, 25, true);
        redisTemplate.opsForValue().setBit(key, 26, true);// 28号没有登录
        redisTemplate.opsForValue().setBit(key, 28, true);// 30号没有登录
    }

    /**
     * 查询userId为911的用户2021年03月22日是否登录
     *
     * @return
     */
    @Test
    public void isLogin() {
        String userId = "911";
        String year = "2021";
        String month = "3";
        String key = userId + "-" + year + "-" + month;
        long offset = 22 - 1;
        Boolean result = redisTemplate.opsForValue().getBit(key, offset);
        System.out.println(result);
    }


    /**
     * 获取userId为911用户03月份第一次没签到是几号
     */
    @Test
    public void firstNoLoginDay() {
        String userId = "911";
        String year = "2021";
        String month = "3";
        String key = userId + "-" + year + "-" + month;

        BitFieldSubCommands b = BitFieldSubCommands.create();
        /*
            Unsigned integers support only up to 63 bits,最大只能是63位
            取无符号31位,即从bitmap中截取31位
         */
        BitFieldSubCommands.BitFieldGetBuilder bitFieldGetBuilder = b.get(BitFieldSubCommands.BitFieldType.unsigned(31));
        BitFieldSubCommands valueAt = bitFieldGetBuilder.valueAt(0);// 从第一位开始

        List<Long> list = redisTemplate.opsForValue().bitField(key, valueAt);//list.get(0)把截取到的30位二进制已经转为10进制了
        String binaryString = Long.toBinaryString(list.get(0));// 将十进制转为2进制

        // There is no restriction on the value of fromIndex. If it is negative, it has
        // the same effect as if it were zero:
        int day = binaryString.indexOf("0");
        if(day == -1) {
            System.out.println("都签到了");
        }else {
            System.out.println(day+1);
        }
    }

    /**
     * 获取userId为911用户2021年3月签到的次数
     */
    @Test
    public void noLogin() {
        String userId = "911";
        String year = "2021";
        String month = "3";
        String key = userId + "-" + year + "-" + month;

        BitFieldSubCommands b = BitFieldSubCommands.create();
        // Unsigned integers support only up to 63 bits,最大只能是63位
        BitFieldSubCommands.BitFieldGetBuilder bitFieldGetBuilder = b.get(BitFieldSubCommands.BitFieldType.unsigned(31));// 取无符号31位
        BitFieldSubCommands valueAt = bitFieldGetBuilder.valueAt(0);// 从第一位开始

        List<Long> list = redisTemplate.opsForValue().bitField(key, valueAt);
        String binaryString = Long.toBinaryString(list.get(0));// 将十进制转为2进制

        String[] origin = binaryString.split("");
        int count = 0;
        for(int i = 0;i < origin.length;i++){
            if("1".equals(origin[i])){
                count++;
            }
        }
        System.out.println(count);

    }

    /**
     * 获取userId为911用户2021年3月签到情况
     */
    @Test
    public void detail() {
        String userId = "911";
        String year = "2021";
        String month = "3";
        String key = userId + "-" + year + "-" + month;


        BitFieldSubCommands b = BitFieldSubCommands.create();
        // Unsigned integers support only up to 63 bits,最大只能是63位
        BitFieldSubCommands.BitFieldGetBuilder bitFieldGetBuilder = b.get(BitFieldSubCommands.BitFieldType.unsigned(31));// 取无符号30位
        BitFieldSubCommands valueAt = bitFieldGetBuilder.valueAt(0);// 从第一位开始

        List<Long> list = redisTemplate.opsForValue().bitField(key, valueAt);
        String binaryString = Long.toBinaryString(list.get(0));// 将十进制转为2进制
        System.out.println(binaryString);
    }

}