package com.elder.pojo.query;

import lombok.Data;

import java.util.Date;

@Data
public class CareItemQuery {
    private String name;
    private Integer status;
    private Date beginCreateTime;
    private Date endCreateTime;
    private Integer page;
    private Integer limit;
}
