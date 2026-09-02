package com.elder.service.impl;

import com.elder.pojo.entity.UserRole;
import com.elder.mapper.UserRoleMapper;
import com.elder.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工-角色关联表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
