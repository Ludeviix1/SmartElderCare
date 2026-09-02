package com.elder.pojo.query;

import lombok.Data;

@Data
public class BedQuery {
    private String building;
    private String roomNo;
    private Integer status;
    private Integer page;
    private Integer limit;
}
