package com.elder.pojo.query;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ElderQuery {
    private String name;
    private String phone;
    /**
     * 标签筛选（支持多个，逗号分隔：tagIds=1,2），要求老人同时拥有所有选中标签
     */
    private List<Long> tagIds;
    private Date beginCreateTime;
    private Date endCreateTime;
    private Integer page;
    private Integer limit;
}
