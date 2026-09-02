package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.ExamPackage;
import com.elder.pojo.query.ExamPackageQuery;
import com.elder.service.IExamPackageService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 体检套餐表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/admin/exam-package")
public class ExamPackageController {

    @Autowired
    private IExamPackageService examPackageService;

    /**
     * 分页查询体检套餐列表
     * GET /exam-package?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<ExamPackage>> list(ExamPackageQuery examPackageQuery) {
        IPage<ExamPackage> page = examPackageService.list(examPackageQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询体检套餐
     * GET /exam-package/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(examPackageService.getById(id));
    }

    /**
     * 新增体检套餐
     * POST /exam-package
     */
    @PostMapping
    public Result add(@RequestBody ExamPackage examPackage) {
        examPackageService.add(examPackage);
        return Result.ok("新增成功");
    }

    /**
     * 修改体检套餐
     * PUT /exam-package/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody ExamPackage examPackage) {
        examPackage.setId(id);
        examPackageService.updateById(examPackage);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除体检套餐
     * DELETE /exam-package/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        examPackageService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除体检套餐
     * DELETE /exam-package
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        examPackageService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}

