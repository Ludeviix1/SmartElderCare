package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.ExamItem;
import com.elder.pojo.query.ExamItemQuery;
import com.elder.service.IExamItemService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 体检项目表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/admin/exam-item")
public class ExamItemController {

    @Autowired
    private IExamItemService examItemService;

    /**
     * 分页查询体检项目列表
     * GET /exam-item?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<ExamItem>> list(ExamItemQuery examItemQuery) {
        IPage<ExamItem> page = examItemService.list(examItemQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询体检项目
     * GET /exam-item/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(examItemService.getById(id));
    }

    /**
     * 新增体检项目
     * POST /exam-item
     */
    @PostMapping
    public Result add(@RequestBody ExamItem examItem) {
        examItemService.add(examItem);
        return Result.ok("新增成功");
    }

    /**
     * 修改体检项目
     * PUT /exam-item/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody ExamItem examItem) {
        examItem.setId(id);
        examItemService.updateById(examItem);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除体检项目
     * DELETE /exam-item/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        examItemService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除体检项目
     * DELETE /exam-item
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        examItemService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}

