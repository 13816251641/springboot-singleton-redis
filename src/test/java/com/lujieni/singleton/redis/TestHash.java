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

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHash {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     * hash赋值
     */
    @Test
    public void test01() {
        redisTemplate.opsForHash().put("student","name","lujieni");
        redisTemplate.opsForHash().put("student","age","29");
    }

    /**
     * 获取单一值
     */
    @Test
    public void test02() {
        String s =(String) redisTemplate.opsForHash().get("student", "name");
        System.out.println(s);
    }

    /**
     * 获得全部,结果以map返回
     * {name=lujieni, age=29}
     */
    @Test
    public void test03() {
        Map<String, Object> map = redisTemplate.opsForHash().entries("student");
        System.out.println(map);
    }

    /**
     * 删除key为student下的2个key和其对应的值
     * student下面如果只有name和age这2个字段的话就student也会没有
     */
    @Test
    public void test04() {
        redisTemplate.opsForHash().delete("student","name","age");
    }

    /**
     * 将对象保存到hash里用的也是比较多的
     */
    @Test
    public void test05() {
        Person person = new Person();
        person.setName("张三");
        person.setAge(27);
        redisTemplate.opsForHash().putAll("person",entityToMap(person));
    }

    @Test
    public void test06() {
        Map<String, Object> map = redisTemplate.opsForHash().entries("person");
        Person person = mapToEntity(map, Person.class);
        System.out.println(person);
    }


    /**
     * 实体类转Map,注意这里的value用的是Object存
     * @param object
     * @return
     */
    public Map<String, Object> entityToMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        /* getDeclaredFields()获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段 */
        for (Field field : object.getClass().getDeclaredFields()){
            try {
                boolean flag = field.isAccessible();
                field.setAccessible(true);
                Object o = field.get(object);
                map.put(field.getName(), o);
                field.setAccessible(flag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }




    /**
     * Map转实体类
     * @param map 需要初始化的数据，key字段必须与实体类的成员名字一样，否则赋值为空
     * @param entity  需要转化成的实体类
     * @return
     */
    public <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
        T t = null;
        try {
            t = entity.newInstance();
            for(Field field : entity.getDeclaredFields()) {
                if (map.containsKey(field.getName())) {
                    boolean flag = field.isAccessible();
                    field.setAccessible(true);
                    Object value = map.get(field.getName());
                    if (value != null) {
                        field.set(t, value);
                    }
                    field.setAccessible(flag);
                }
            }
            return t;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return t;
    }


}