package com.lujieni.singleton.redis.dao;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import com.lujieni.singleton.redis.mybatis.basedao.BaseDao;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentDao extends BaseDao<StudentPO> {


}
