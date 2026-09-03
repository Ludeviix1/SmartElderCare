package com.elder.pojo.query;

import lombok.Data;

@Data
public class ExamAppointmentQuery {
    private Long elderId;
    private Long packageId;
    private Long caregiverId;
    private Integer assignmentStatus;
    private Integer status;
    private String beginDate;
    private String endDate;
    private long page = 1;
    private long limit = 10;
}
