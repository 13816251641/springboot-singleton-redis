package com.lujieni.singleton.redis;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringApplicationContext implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void test01() throws Exception{
        Mapper<StudentPO> mapper = (Mapper) applicationContext.getBean(Class.forName("com.lujieni.singleton.redis.dao.StudentDao"));
        List<StudentPO> list = mapper.selectAll();
        System.out.println(list);
    }

    @Test
    public void test02() throws Exception{
        Mapper<StudentPO> mapper = (Mapper) applicationContext.getBean("studentDao");
        List<StudentPO> list = mapper.selectAll();
        System.out.println(list);
    }
}