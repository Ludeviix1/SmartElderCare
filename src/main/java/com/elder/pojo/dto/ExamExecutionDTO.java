package com.elder.pojo.dto;

import com.elder.pojo.entity.ExamAppointmentItem;
import lombok.Data;
import java.util.List;

@Data
public class ExamExecutionDTO {
    private String remark;
    private List<ExamAppointmentItem> items;
}
