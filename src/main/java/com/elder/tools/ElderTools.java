package com.elder.tools;

import com.elder.pojo.vo.ElderInfoVO;
import com.elder.pojo.vo.ExamAppointmentVO;
import com.elder.service.IElderService;
import com.elder.service.IExamAppointmentService;
import com.elder.service.ICarePlanService;
import com.elder.service.ICareTaskService;
import com.elder.pojo.entity.CarePlan;
import com.elder.pojo.entity.CareTask;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Tools exposed only for the current elder's conversation. */
public class ElderTools {

    private final Long elderId;
    private final IElderService elderService;
    private final IExamAppointmentService examAppointmentService;
    private final ICarePlanService carePlanService;
    private final ICareTaskService careTaskService;

    public ElderTools(Long elderId, IElderService elderService, IExamAppointmentService examAppointmentService,
                      ICarePlanService carePlanService, ICareTaskService careTaskService) {
        this.elderId = elderId;
        this.elderService = elderService;
        this.examAppointmentService = examAppointmentService;
        this.carePlanService = carePlanService;
        this.careTaskService = careTaskService;
    }

    @Tool(description = "查询当前登录老人的基础资料。当用户询问自己的姓名、年龄、住址或联系方式时调用。不得用来查询其他老人。")
    public Map<String, Object> getCurrentElderInfo() {
        ElderInfoVO elder = elderService.getElderInfo(elderId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("姓名", elder.getName());
        result.put("年龄", elder.getAge());
        result.put("联系电话", elder.getPhone());
        result.put("住址", elder.getAddress());
        return result;
    }

    @Tool(description = "查询当前登录老人的体检预约，包括套餐、预约日期时间和状态。用户询问体检预约、体检时间或是否已完成时调用。")
    public List<Map<String, Object>> getMyExamAppointments() {
        return examAppointmentService.listByElderId(elderId).stream()
                .map(this::appointmentSummary)
                .toList();
    }

    @Tool(description = "查询当前老人的指定体检预约报告摘要。仅当用户明确提供预约编号并询问该报告时调用。")
    public Map<String, Object> getMyExamReport(@ToolParam(description = "体检预约编号") Long appointmentId) {
        ExamAppointmentVO appointment = examAppointmentService.listByElderId(elderId).stream()
                .filter(item -> item.getId().equals(appointmentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未找到该预约，或无权查看"));
        if (appointment.getStatus() != 2) {
            return Map.of("status", "体检尚未完成，暂时没有报告", "appointmentId", appointmentId);
        }
        return examAppointmentService.executionDetail(appointmentId);
    }

    @Tool(description = "查询当前老人生效中的护理计划，包括计划名称、护理等级和起止日期。用户询问护理计划或护理安排时调用。")
    public List<Map<String, Object>> getMyActiveCarePlans() {
        return carePlanService.lambdaQuery()
                .eq(CarePlan::getElderId, elderId)
                .eq(CarePlan::getStatus, 1)
                .orderByDesc(CarePlan::getCreateTime)
                .list()
                .stream()
                .map(this::carePlanSummary)
                .toList();
    }

    @Tool(description = "查询当前老人待执行的护理任务，包括护理项目、计划执行日期和时间。用户询问今天或近期要做什么护理时调用。")
    public List<Map<String, Object>> getMyPendingCareTasks() {
        return careTaskService.lambdaQuery()
                .eq(CareTask::getElderId, elderId)
                .eq(CareTask::getStatus, 0)
                .orderByAsc(CareTask::getPlanExecuteDate)
                .orderByAsc(CareTask::getPlanExecuteTime)
                .last("LIMIT 10")
                .list()
                .stream()
                .map(this::careTaskSummary)
                .toList();
    }

    @Tool(description = "查询当前老人最近完成的十条护理记录，包括护理项目、完成时间和执行结果。用户询问最近做过什么护理、护理是否完成时调用。")
    public List<Map<String, Object>> getMyRecentCareRecords() {
        return careTaskService.lambdaQuery()
                .eq(CareTask::getElderId, elderId)
                .eq(CareTask::getStatus, 1)
                .orderByDesc(CareTask::getActualExecuteTime)
                .last("LIMIT 10")
                .list()
                .stream()
                .map(this::careTaskSummary)
                .toList();
    }

    private Map<String, Object> appointmentSummary(ExamAppointmentVO appointment) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("预约编号", appointment.getId());
        result.put("体检套餐", appointment.getPackageName());
        result.put("预约日期", appointment.getAppointmentDate());
        result.put("预约时间", appointment.getAppointmentTime());
        result.put("体检状态", examStatusText(appointment.getStatus()));
        result.put("分配护工", appointment.getCaregiverName() == null ? "暂未分配" : appointment.getCaregiverName());
        result.put("项目数量", appointment.getExamItemCount());
        return result;
    }

    private Map<String, Object> carePlanSummary(CarePlan plan) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("计划编号", plan.getId());
        result.put("计划名称", plan.getName());
        result.put("开始日期", plan.getStartDate());
        result.put("结束日期", plan.getEndDate());
        result.put("状态", plan.getStatus() == 1 ? "执行中" : "已结束");
        return result;
    }

    private Map<String, Object> careTaskSummary(CareTask task) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("任务编号", task.getId());
        result.put("护理项目", task.getCareItemName());
        result.put("计划日期", task.getPlanExecuteDate());
        result.put("计划时间", task.getPlanExecuteTime());
        result.put("任务状态", careTaskStatusText(task.getStatus()));
        if (task.getActualExecuteTime() != null) result.put("完成时间", task.getActualExecuteTime());
        if (task.getExecuteResult() != null) result.put("执行结果", task.getExecuteResult());
        if (task.getRemark() != null) result.put("护工备注", task.getRemark());
        return result;
    }

    private String examStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "待体检";
            case 1 -> "体检中";
            case 2 -> "已完成";
            case 3 -> "已取消";
            case 4 -> "已过期";
            default -> "未知";
        };
    }

    private String careTaskStatusText(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 0 -> "待执行";
            case 1 -> "已完成";
            case 2 -> "已跳过或取消";
            default -> "未知";
        };
    }
}
