package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.mapper.RolePermissionMapper;
import com.elder.pojo.entity.Role;
import com.elder.mapper.RoleMapper;
import com.elder.pojo.entity.RolePermission;
import com.elder.pojo.query.RoleQuery;
import com.elder.pojo.vo.PermissionVO;
import com.elder.service.IPermissionService;
import com.elder.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;



    @Override
    public IPage<Role> list(RoleQuery roleQuery) {
        IPage<Role> page = new Page<>(roleQuery.getPage(), roleQuery.getLimit());

        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(roleQuery.getName()), Role::getName, roleQuery.getName())
                .between(!ObjectUtils.isEmpty(roleQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(roleQuery.getEndCreateTime()), Role::getCreateTime, roleQuery.getBeginCreateTime(), roleQuery.getEndCreateTime())
                .orderByDesc(Role::getCreateTime);

        return roleMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public Map<String, Object> selectAssignedRole(Long roleId) {
        // 所有权限树形结构
        List<PermissionVO> permissionVOList = permissionService.selectPermissionTree();
        // 根据roleId去role_permission中查询这个角色已经分配权限的id集合
        LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RolePermission::getRoleId, roleId);
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(lambdaQueryWrapper);
        List<Long> assignedPermissionIdList = rolePermissionList.stream().map(RolePermission::getPermissionId).toList();

        Map<String, Object> map = new HashMap<>();
        map.put("permissionVOList", permissionVOList);
        map.put("assignedPermissionIdList", assignedPermissionIdList);
        return map;
    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        rolePermissionMapper.delete(new LambdaQueryWrapper<RolePermission>().eq(RolePermission::getRoleId, roleId));
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }
}
