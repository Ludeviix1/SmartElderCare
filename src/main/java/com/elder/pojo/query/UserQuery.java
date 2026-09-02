package com.elder.pojo.query;

import lombok.Data;

import java.util.Date;

@Data
public class UserQuery {
    private String name;
    private String email;
    private Date beginCreateTime;
    private Date endCreateTime;
    private Integer page;
    private Integer limit;
}
