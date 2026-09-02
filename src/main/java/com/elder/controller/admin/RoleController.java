package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Role;
import com.elder.pojo.query.RoleQuery;
import com.elder.service.IRoleService;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @GetMapping("/selectAssignedPermission/{roleId}")
    public Result selectAssignedRole(@PathVariable("roleId") Long roleId) {
        Map<String, Object> map = roleService.selectAssignedRole(roleId);
        return Result.ok(map);
    }

    @PostMapping("/assignPermission")
    public Result assignPermission(Long roleId, Long[] permissionIds) {
        roleService.assignPermission(roleId, permissionIds);
        return Result.ok("分配成功");
    }

    /**
     * 分页查询角色列表
     * GET /role?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<Role>> list(RoleQuery roleQuery) {
        IPage<Role> page = roleService.list(roleQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询角色
     * GET /role/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(roleService.getById(id));
    }

    /**
     * 新增角色
     * POST /role
     */
    @PostMapping
    public Result add(@RequestBody Role role) {
        roleService.save(role);
        return Result.ok("新增成功");
    }

    /**
     * 修改角色
     * PUT /role/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        roleService.updateById(role);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除角色（逻辑删除）
     * DELETE /role/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除角色
     * DELETE /role
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        roleService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
