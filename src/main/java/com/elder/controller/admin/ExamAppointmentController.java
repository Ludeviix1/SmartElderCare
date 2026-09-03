package com.elder.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.dto.AdminAppointmentDTO;
import com.elder.pojo.query.ExamAppointmentQuery;
import com.elder.pojo.vo.ExamAppointmentVO;
import com.elder.service.IExamAppointmentService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/exam-appointment")
public class ExamAppointmentController {
    @Autowired private IExamAppointmentService service;

    @GetMapping public Result<IPage<ExamAppointmentVO>> list(ExamAppointmentQuery query) { return Result.ok(service.listAdmin(query)); }
    @PostMapping public Result add(@RequestBody AdminAppointmentDTO dto) { service.addAdmin(dto); return Result.ok("预约成功"); }
    @PutMapping("/{id}/assign") public Result assign(@PathVariable Long id, @RequestParam(required = false) Long caregiverId) { service.assign(id, caregiverId); return Result.ok("分配成功"); }
    @PostMapping("/{id}/auto-assign") public Result autoAssign(@PathVariable Long id) { return Result.ok(service.autoAssign(id)); }
    @DeleteMapping("/{id}") public Result delete(@PathVariable Long id) { service.removeById(id); return Result.ok("删除成功"); }
}
