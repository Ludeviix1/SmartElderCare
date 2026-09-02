package com.elder.service;

import com.elder.pojo.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.vo.PermissionVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
public interface IPermissionService extends IService<Permission> {

    List<PermissionVO> selectPermissionTree();

    Map<String, Object> selectPermissionByUserId(Long id);
}
