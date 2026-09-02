package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Tag;
import com.elder.pojo.query.TagQuery;
import com.elder.service.ITagService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
@RestController
@RequestMapping("/admin/tag")
public class TagController {

    @Autowired
    private ITagService tagService;

    /**
     * 分页查询标签列表
     * GET /tag?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<Tag>> list(TagQuery tagQuery) {
        IPage<Tag> page = tagService.list(tagQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询标签
     * GET /tag/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(tagService.getById(id));
    }

    /**
     * 新增标签
     * POST /tag
     */
    @PostMapping
    public Result add(@RequestBody Tag tag) {
        tagService.save(tag);
        return Result.ok("新增成功");
    }

    /**
     * 修改标签
     * PUT /tag/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Tag tag) {
        tag.setId(id);
        tagService.updateById(tag);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除标签（逻辑删除）
     * DELETE /tag/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        tagService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除标签
     * DELETE /tag
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        tagService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
