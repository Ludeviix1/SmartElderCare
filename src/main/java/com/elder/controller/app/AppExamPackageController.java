package com.elder.controller.app;

import com.elder.pojo.vo.ExamPackageDetailVO;
import com.elder.pojo.vo.ExamPackageVO;
import com.elder.service.IExamPackageService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 老人手机端体检套餐控制器
 *
 * @author Gronru
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/app/exam-package")
public class AppExamPackageController {

    @Autowired
    private IExamPackageService examPackageService;

    /**
     * 上架的套餐列表（含项目数量）
     * GET /app/exam-package
     */
    @GetMapping
    public Result<List<ExamPackageVO>> list() {
        return Result.ok(examPackageService.listOnShelf());
    }

    /**
     * 套餐详情（含包含的体检项目）
     * GET /app/exam-package/1
     */
    @GetMapping("/{id}")
    public Result<ExamPackageDetailVO> getById(@PathVariable Long id) {
        return Result.ok(examPackageService.selectDetailById(id));
    }
}
