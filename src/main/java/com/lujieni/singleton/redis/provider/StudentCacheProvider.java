package com.lujieni.singleton.redis.provider;

import com.lujieni.singleton.redis.dao.StudentDao;
import com.lujieni.singleton.redis.domain.po.StudentPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentCacheProvider implements ITTLCacheProvider<StudentPO> {

    @Autowired
    private StudentDao studentDao;

    /**
     * 加载单个元素
     * @param key
     * @return
     */
    @Override
    public StudentPO get(String key) {
        return studentDao.selectByPrimaryKey(Integer.parseInt(key));
    }
}