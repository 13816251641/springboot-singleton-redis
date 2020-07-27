package com.lujieni.singleton.redis.dao;

import com.lujieni.singleton.redis.domain.po.StudentPO;
import com.lujieni.singleton.redis.mybatis.basedao.BaseDao;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends BaseDao<StudentPO> {


}
