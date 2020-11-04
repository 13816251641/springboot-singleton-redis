package com.lujieni.singleton.redis.cache;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCache {

    @Autowired
    private StudentCache studentCache;

    @Test
    public void test01(){
        /*
            redis中是可以把value=null的数据存入的
         */
        StudentPO studentPO = studentCache.get("100");
        System.out.println("studentPO:"+studentPO);

        StudentPO studentPO2 = studentCache.get("100");
        System.out.println("studentPO2:"+studentPO2);

    }
}