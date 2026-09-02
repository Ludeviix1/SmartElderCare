package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.CareLevel;
import com.elder.pojo.query.CareLevelQuery;
import com.elder.service.ICareLevelService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 护理等级表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
@RestController
@RequestMapping("/admin/care-level")
public class CareLevelController {

    @Autowired
    private ICareLevelService careLevelService;

    /**
     * 分页查询护理等级列表
     * GET /care-level?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<CareLevel>> list(CareLevelQuery careLevelQuery) {
        IPage<CareLevel> page = careLevelService.list(careLevelQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询护理等级
     * GET /care-level/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(careLevelService.getById(id));
    }

    /**
     * 新增护理等级
     * POST /care-level
     */
    @PostMapping
    public Result add(@RequestBody CareLevel careLevel) {
        careLevelService.add(careLevel);
        return Result.ok("新增成功");
    }

    /**
     * 修改护理等级
     * PUT /care-level/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody CareLevel careLevel) {
        careLevel.setId(id);
        careLevelService.updateById(careLevel);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除护理等级
     * DELETE /care-level/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        careLevelService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除护理等级
     * DELETE /care-level
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        careLevelService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
