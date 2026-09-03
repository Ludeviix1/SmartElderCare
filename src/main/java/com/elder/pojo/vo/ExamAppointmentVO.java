package com.elder.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 老人手机端预约列表VO：预约信息连同套餐名、体检人姓名
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
public class ExamAppointmentVO {

    /**
     * 预约ID
     */
    private Long id;

    /**
     * 体检套餐ID
     */
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 体检人姓名
     */
    private String elderName;

    /**
     * 预约日期（yyyy-MM-dd）
     */
    private String appointmentDate;

    /**
     * 预约时间（HH:mm）
     */
    private String appointmentTime;

    /**
     * 预约时的套餐价格
     */
    private BigDecimal price;

    /**
     * 状态：0待体检 1体检中 2已完成 3已取消 4已过期
     */
    private Integer status;

    private Long caregiverId;
    private String caregiverName;
    private Integer assignmentStatus;

    /** 预约备注 */
    private String remark;

    /**
     * 套餐包含的体检项目数量
     */
    private Integer examItemCount;

}
