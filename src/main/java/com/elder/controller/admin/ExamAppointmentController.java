package com.elder.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.dto.AdminAppointmentDTO;
import com.elder.pojo.dto.ExamExecutionDTO;
import com.elder.service.IUserService;
import com.elder.util.JwtUtil;
import com.elder.pojo.query.ExamAppointmentQuery;
import com.elder.pojo.vo.ExamAppointmentVO;
import com.elder.service.IExamAppointmentService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/admin/exam-appointment")
public class ExamAppointmentController {
    @Autowired private IExamAppointmentService service;
    @Autowired private IUserService userService;

    @GetMapping public Result<IPage<ExamAppointmentVO>> list(ExamAppointmentQuery query, @RequestHeader("Authorization") String token) {
        Long userId = Long.valueOf(JwtUtil.parseToken(token).get("id").toString());
        if (userService.hasRoleCode(userId, "hugong")) query.setCaregiverId(userId);
        return Result.ok(service.listAdmin(query));
    }
    @PostMapping public Result add(@RequestBody AdminAppointmentDTO dto) { service.addAdmin(dto); return Result.ok("预约成功"); }
    @PutMapping("/{id}/assign") public Result assign(@PathVariable Long id, @RequestParam(required = false) Long caregiverId) { service.assign(id, caregiverId); return Result.ok("分配成功"); }
    @PostMapping("/{id}/auto-assign") public Result autoAssign(@PathVariable Long id) { return Result.ok(service.autoAssign(id)); }
    @DeleteMapping("/{id}") public Result delete(@PathVariable Long id) { service.removeById(id); return Result.ok("删除成功"); }
    @GetMapping("/{id}/execution") public Result detail(@PathVariable Long id) { return Result.ok(service.executionDetail(id)); }
    @PutMapping("/{id}/execution") public Result execute(@PathVariable Long id, @RequestBody ExamExecutionDTO dto, @RequestHeader("Authorization") String token) {
        Long userId = Long.valueOf(JwtUtil.parseToken(token).get("id").toString());
        service.execute(id, dto, userId, userService.hasRoleCode(userId, "hugong"));
        return Result.ok("体检执行已完成");
    }
}
