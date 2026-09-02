package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.CareItem;
import com.elder.pojo.query.CareItemQuery;
import com.elder.service.ICareItemService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 护理项目表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
@RestController
@RequestMapping("/admin/care-item")
public class CareItemController {

    @Autowired
    private ICareItemService careItemService;

    /**
     * 分页查询护理项目列表
     * GET /care-item?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<CareItem>> list(CareItemQuery careItemQuery) {
        IPage<CareItem> page = careItemService.list(careItemQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询护理项目
     * GET /care-item/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(careItemService.getById(id));
    }

    /**
     * 新增护理项目
     * POST /care-item
     */
    @PostMapping
    public Result add(@RequestBody CareItem careItem) {
        careItemService.add(careItem);
        return Result.ok("新增成功");
    }

    /**
     * 修改护理项目
     * PUT /care-item/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody CareItem careItem) {
        careItem.setId(id);
        careItemService.updateById(careItem);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除护理项目
     * DELETE /care-item/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        careItemService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除护理项目
     * DELETE /care-item
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        careItemService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}

