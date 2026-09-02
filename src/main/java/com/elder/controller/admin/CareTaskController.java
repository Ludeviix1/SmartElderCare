package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.CareTask;
import com.elder.pojo.query.CareTaskQuery;
import com.elder.pojo.vo.CareTaskVO;
import com.elder.service.ICareTaskService;
import com.elder.service.IUserService;
import com.elder.util.JwtUtil;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 护理任务与打卡记录表 前端控制器
 * </p>
 *
 * 护理任务由护理计划生成，不提供新增接口
 *
 * @author Gronru
 * @since 2026-09-01
 */
@RestController
@RequestMapping("/admin/care-task")
public class CareTaskController {

    @Autowired
    private ICareTaskService careTaskService;
    @Autowired
    private IUserService userService;

    /**
     * 分页查询护理任务列表（附带老人、护理员、护理计划名称）
     * GET /care-task?page=1&limit=10&elderId=1&status=0
     * 护工（hugong）角色登录时只能看到分配给自己的任务
     */
    @GetMapping
    public Result<IPage<CareTaskVO>> list(CareTaskQuery careTaskQuery,
                                          @RequestHeader("Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        Long currentUserId = Long.valueOf(map.get("id").toString());
        if (userService.hasRoleCode(currentUserId, "hugong")) {
            careTaskQuery.setUserId(currentUserId);
        }
        IPage<CareTaskVO> page = careTaskService.list(careTaskQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询护理任务
     * GET /care-task/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(careTaskService.getById(id));
    }

    /**
     * 修改护理任务（状态、执行打卡信息）
     * PUT /care-task/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody CareTask careTask) {
        careTask.setId(id);
        careTaskService.updateById(careTask);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除护理任务
     * DELETE /care-task/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        careTaskService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除护理任务
     * DELETE /care-task
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        careTaskService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
