package com.elder.controller.app;

import com.elder.pojo.dto.AppAppointmentDTO;
import com.elder.pojo.vo.ExamAppointmentVO;
import com.elder.service.IExamAppointmentService;
import com.elder.util.JwtUtil;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 老人手机端体检预约控制器
 *
 * @author Gronru
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/app/appointment")
public class AppAppointmentController {

    @Autowired
    private IExamAppointmentService examAppointmentService;

    /**
     * 提交体检预约
     * POST /app/appointment
     */
    @PostMapping
    public Result add(@RequestHeader("Authorization") String token,
                      @RequestBody AppAppointmentDTO appAppointmentDTO) {
        Long elderId = getElderIdFromToken(token);
        examAppointmentService.add(appAppointmentDTO, elderId);
        return Result.ok("预约成功");
    }

    /**
     * 我的预约列表
     * GET /app/appointment
     */
    @GetMapping
    public Result<List<ExamAppointmentVO>> list(@RequestHeader("Authorization") String token) {
        Long elderId = getElderIdFromToken(token);
        return Result.ok(examAppointmentService.listByElderId(elderId));
    }

    /**
     * 取消预约
     * PUT /app/appointment/1/cancel
     */
    @PutMapping("/{id}/cancel")
    public Result cancel(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long elderId = getElderIdFromToken(token);
        examAppointmentService.cancel(id, elderId);
        return Result.ok("取消成功");
    }

    /** 查看已完成体检报告，只允许查看本人的预约 */
    @GetMapping("/{id}/report")
    public Result report(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        Long elderId = getElderIdFromToken(token);
        ExamAppointmentVO appointment = examAppointmentService.listByElderId(elderId).stream()
                .filter(item -> item.getId().equals(id)).findFirst()
                .orElseThrow(() -> new com.elder.exception.ServiceException("预约不存在或无权查看"));
        if (appointment.getStatus() != 2) {
            return Result.error("体检尚未完成，暂不能查看报告");
        }
        return Result.ok(examAppointmentService.executionDetail(id));
    }

    /**
     * 从token中解析当前登录老人的id
     */
    private Long getElderIdFromToken(String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer id = (Integer) map.get("id");
        return id.longValue();
    }
}
