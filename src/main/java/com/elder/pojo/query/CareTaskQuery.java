package com.elder.pojo.query;

import lombok.Data;

import java.util.Date;

@Data
public class CareTaskQuery {
    private Long elderId;
    private Long careItemId;
    //护理员ID（护工角色登录时由后端强制赋值为当前用户）
    private Long userId;
    private Integer status;
    private Date beginPlanExecuteDate;
    private Date endPlanExecuteDate;
    private Integer page;
    private Integer limit;
}
