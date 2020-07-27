package com.lujieni.singleton.redis.mybatis;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class BasicEntity {
    @Id
    @Column(
            name = "id"
    )
    private Integer id;

}