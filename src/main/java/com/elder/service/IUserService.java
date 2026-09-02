package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.UserQuery;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-24
 */
public interface IUserService extends IService<User> {

    IPage<User> list(UserQuery userQuery);

    List<User> listByRoleCode(String roleCode);

    boolean hasRoleCode(Long userId, String roleCode);

    void exportExcel(HttpServletResponse response);

    void importExcel(MultipartFile file);

    void add(User user);

    Map<String, Object> selectAssignedRole(Long userId);

    void assignRole(Long userId, Long[] roleIds);
}
