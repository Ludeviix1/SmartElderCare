package com.elder.service;

import com.elder.pojo.dto.AppAppointmentDTO;
import com.elder.pojo.entity.ExamAppointment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.vo.ExamAppointmentVO;
import com.elder.pojo.query.ExamAppointmentQuery;
import com.elder.pojo.dto.AdminAppointmentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * <p>
 * 体检预约表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
public interface IExamAppointmentService extends IService<ExamAppointment> {

    /**
     * 提交体检预约（同时写入套餐内项目的快照）
     */
    void add(AppAppointmentDTO appAppointmentDTO, Long elderId);

    /**
     * 查询老人的预约列表（含套餐名、体检人姓名、项目数）
     */
    List<ExamAppointmentVO> listByElderId(Long elderId);

    /**
     * 取消预约（只能取消自己的、待体检状态的预约）
     */
    void cancel(Long id, Long elderId);

    IPage<ExamAppointmentVO> listAdmin(ExamAppointmentQuery query);
    void addAdmin(AdminAppointmentDTO dto);
    void assign(Long id, Long caregiverId);
    Long autoAssign(Long id);
}
