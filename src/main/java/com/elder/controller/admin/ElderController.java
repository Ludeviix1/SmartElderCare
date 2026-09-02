package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.query.ElderQuery;
import com.elder.pojo.vo.ElderVO;
import com.elder.service.IElderService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 老人表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
@RestController
@RequestMapping("/admin/elders")
public class ElderController {

    @Autowired
    private IElderService elderService;

    // /elders/assignTag?elderId=1&tagIds=1,2,3
    @PostMapping("/assignTag")
    public Result assignTag(Long elderId, Long[] tagIds) {
        elderService.assignTag(elderId, tagIds);
        return Result.ok("分配标签成功");
    }

    @GetMapping("/selectAssignedTag/{elderId}")
    public Result selectAssignedTag(@PathVariable("elderId") Long elderId) {
        Map<String, Object> map = elderService.selectAssignedTag(elderId);
        return Result.ok(map);
    }

    /**
     * 分页查询老人列表（附带每个老人已分配的标签名）
     * GET /elder?page=1&limit=10&name=xxx&phone=xxx
     */
    @GetMapping
    public Result<IPage<ElderVO>> list(ElderQuery elderQuery) {
        IPage<ElderVO> page = elderService.list(elderQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询老人
     * GET /elder/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(elderService.getById(id));
    }

    /**
     * 新增老人
     * POST /elder
     */
    @PostMapping
    public Result add(@RequestBody Elder elder) {
        elderService.save(elder);
        return Result.ok("新增成功");
    }

    /**
     * 修改老人
     * PUT /elder/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Elder elder) {
        elder.setId(id);
        elderService.updateById(elder);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除老人（逻辑删除）
     * DELETE /elder/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        elderService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除老人
     * DELETE /elder
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        elderService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
