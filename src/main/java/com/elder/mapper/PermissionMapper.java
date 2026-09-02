package com.elder.mapper;

import com.elder.pojo.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 权限表 Mapper 接口
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> selectPermissionByUserId(Long id);
}
