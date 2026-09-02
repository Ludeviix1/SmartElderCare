package com.elder.pojo.query;

import lombok.Data;

@Data
public class FamilyMemberQuery {
    private String name;
    private String phone;
    private Long elderId;
    private Integer page;
    private Integer limit;
}
