package com.elder.pojo.dto;

import lombok.Data;

/**
 * 老人手机端提交体检预约DTO
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
public class AppAppointmentDTO {

    /**
     * 体检套餐ID
     */
    private Long packageId;

    /**
     * 预约日期（yyyy-MM-dd）
     */
    private String date;

    /**
     * 预约时间（HH:mm）
     */
    private String time;

}
