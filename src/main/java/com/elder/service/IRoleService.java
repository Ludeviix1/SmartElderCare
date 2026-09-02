package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.RoleQuery;

import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
public interface IRoleService extends IService<Role> {

    IPage<Role> list(RoleQuery roleQuery);

    Map<String, Object> selectAssignedRole(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);
}
