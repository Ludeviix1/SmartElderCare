package com.elder.controller.admin;

import com.elder.pojo.entity.Bed;
import com.elder.pojo.entity.CareTask;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.entity.ExamAppointment;
import com.elder.service.IBedService;
import com.elder.service.ICareTaskService;
import com.elder.service.IElderService;
import com.elder.service.IExamAppointmentService;
import com.elder.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
public class DashboardController {
    private final IElderService elderService;
    private final IBedService bedService;
    private final ICareTaskService careTaskService;
    private final IExamAppointmentService examAppointmentService;

    public DashboardController(IElderService elderService, IBedService bedService,
                               ICareTaskService careTaskService, IExamAppointmentService examAppointmentService) {
        this.elderService = elderService;
        this.bedService = bedService;
        this.careTaskService = careTaskService;
        this.examAppointmentService = examAppointmentService;
    }

    @GetMapping("/overview")
    public Result<Map<String, Object>> overview() {
        LocalDate today = LocalDate.now();
        Date todayStart = atStartOfDay(today);
        Date tomorrowStart = atStartOfDay(today.plusDays(1));
        long residentCount = elderService.lambdaQuery().eq(Elder::getStatus, 4).count();
        long occupiedBeds = bedService.lambdaQuery().eq(Bed::getStatus, 1).count();
        long availableBeds = bedService.lambdaQuery().eq(Bed::getStatus, 0).count();
        long todayTasks = careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, todayStart)
                .lt(CareTask::getPlanExecuteDate, tomorrowStart).count();
        long completedToday = careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, todayStart)
                .lt(CareTask::getPlanExecuteDate, tomorrowStart).eq(CareTask::getStatus, 1).count();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("residentCount", residentCount);
        data.put("occupiedBeds", occupiedBeds);
        data.put("availableBeds", availableBeds);
        data.put("totalBeds", occupiedBeds + availableBeds + bedService.lambdaQuery().eq(Bed::getStatus, 2).count());
        data.put("todayTasks", todayTasks);
        data.put("completedToday", completedToday);
        data.put("examPending", examAppointmentService.lambdaQuery().eq(ExamAppointment::getStatus, 0).count());
        data.put("examCompleted", examAppointmentService.lambdaQuery().eq(ExamAppointment::getStatus, 2).count());

        List<String> dates = new ArrayList<>();
        List<Long> taskTotals = new ArrayList<>();
        List<Long> taskCompleted = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(6 - i);
            Date start = atStartOfDay(date);
            Date end = atStartOfDay(date.plusDays(1));
            dates.add(date.toString());
            taskTotals.add(careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, start)
                    .lt(CareTask::getPlanExecuteDate, end).count());
            taskCompleted.add(careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, start)
                    .lt(CareTask::getPlanExecuteDate, end).eq(CareTask::getStatus, 1).count());
        }
        data.put("trendDates", dates);
        data.put("taskTotals", taskTotals);
        data.put("taskCompleted", taskCompleted);
        data.put("taskPending", careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, todayStart)
                .lt(CareTask::getPlanExecuteDate, tomorrowStart).eq(CareTask::getStatus, 0).count());
        data.put("taskSkipped", careTaskService.lambdaQuery().ge(CareTask::getPlanExecuteDate, todayStart)
                .lt(CareTask::getPlanExecuteDate, tomorrowStart).eq(CareTask::getStatus, 2).count());
        return Result.ok(data);
    }

    private Date atStartOfDay(LocalDate date) {
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
