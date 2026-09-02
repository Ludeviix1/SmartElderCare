package com.elder.controller.admin;


import com.elder.service.IExamPackageItemService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 体检套餐项目关联表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/admin/exam-package-item")
public class ExamPackageItemController {

    @Autowired
    private IExamPackageItemService examPackageItemService;

    /**
     * 查询套餐已分配的体检项目ID列表
     * GET /admin/exam-package-item/1
     */
    @GetMapping("/{packageId}")
    public Result<List<Long>> listExamItemIds(@PathVariable Long packageId) {
        return Result.ok(examPackageItemService.getExamItemIdsByPackageId(packageId));
    }

    /**
     * 给套餐分配体检项目（全量覆盖）
     * PUT /admin/exam-package-item/1
     */
    @PutMapping("/{packageId}")
    public Result assignExamItems(@PathVariable Long packageId, @RequestBody List<Long> examItemIds) {
        examPackageItemService.assignExamItems(packageId, examItemIds);
        return Result.ok("分配成功");
    }
}
