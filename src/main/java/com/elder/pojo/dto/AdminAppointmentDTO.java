package com.elder.pojo.dto;

import lombok.Data;

@Data
public class AdminAppointmentDTO {
    private Long elderId;
    private Long packageId;
    private String date;
    private String time;
    private String remark;
}
