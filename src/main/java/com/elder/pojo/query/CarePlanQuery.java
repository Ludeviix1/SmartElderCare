package com.elder.pojo.query;

import lombok.Data;

import java.util.Date;

@Data
public class CarePlanQuery {
    private String name;
    private Long elderId;
    private Long userId;
    private Long careLevelId;
    private Integer status;
    private Date beginCreateTime;
    private Date endCreateTime;
    private Integer page;
    private Integer limit;
}
