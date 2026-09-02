package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elder.pojo.entity.Permission;
import com.elder.mapper.PermissionMapper;
import com.elder.pojo.vo.PermissionVO;
import com.elder.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionVO> selectPermissionTree() {
        //1.查找所有分类，按排序升序
        LambdaQueryWrapper<Permission> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Permission::getSort);
        List<Permission> permissionList = permissionMapper.selectList(lambdaQueryWrapper);
        //2.将permissionList转换成PermissionVOList
        List<PermissionVO> permissionVOList = permissionList.stream().map(permission -> {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            return permissionVO;
        }).toList();

        //3.将permissionVOList转换成PermissionTree
        /*List<PermissionVO> permissionTree = permissionVOList.stream()
                .filter(permissionVO -> permissionVO.getParentId() == 0)
                .map(permissionVO -> {// 所有的一级 parentId=0
                    permissionVO.setChildren(permissionVOList.stream().
                            filter(permissionVO2 -> permissionVO2.getParentId().equals(permissionVO.getId())).toList());
                    return permissionVO;
        }).toList();*/

        //所有一级分类
        /*List<PermissionVO> permissionVOTree = permissionVOList.stream()
                .filter(permissionVO -> permissionVO.getParentId() == 0)
                .map(permissionVO -> {
                    permissionVO.setChildren(buildChildrenTree(permissionVO, permissionVOList)); // 构建children
                    return permissionVO;
                }).toList();*/
        List<PermissionVO> permissionVOTree = buildTree(permissionVOList);
        return permissionVOTree;
    }

    /**
     * 给我任何一个List<PermissionVO>，返回一个树形结构
     * @param permissionVOList
     * @return
     */
    public List<PermissionVO> buildTree(List<PermissionVO> permissionVOList) {
        //所有一级分类
        List<PermissionVO> permissionVOTree = permissionVOList.stream()
                .filter(permissionVO -> permissionVO.getParentId() == 0)
                .map(permissionVO -> {
                    permissionVO.setChildren(buildChildrenTree(permissionVO, permissionVOList)); // 构建children
                    return permissionVO;
                }).toList();
        return permissionVOTree;
    }

    @Override
    public Map<String, Object> selectPermissionByUserId(Long id) {
        //根据用户id查询这个用户所有权限
        List<Permission> permissionList = permissionMapper.selectPermissionByUserId(id);
        //把List<Permission> 转换成List<PermissionVO>
        List<String> btnList = new ArrayList<>();
        List<PermissionVO> permissionVOList = new ArrayList<>();
        permissionList.forEach(permission -> {
            if (permission.getType() == 2) {//按钮权限
                btnList.add(permission.getPermissionValue());
            } else {//目录和菜单权限
                PermissionVO permissionVO = new PermissionVO();
                BeanUtils.copyProperties(permission, permissionVO);
                permissionVOList.add(permissionVO);
            }
        });


        return Map.of("routerList", buildTree(permissionVOList), "btnList", btnList);
    }

    /**
     * 构建子节点树
     * @param parentPermissionVO 父节点
     * @param permissionVOList  所有孩子集合
     * @return
     */
    private List<PermissionVO> buildChildrenTree(PermissionVO parentPermissionVO, List<PermissionVO> permissionVOList) {
        return permissionVOList.stream()
                .filter(permissionVO -> permissionVO.getParentId().equals(parentPermissionVO.getId()))
                .map(permissionVO -> {// 构建孩子节点的 children
                    permissionVO.setChildren(buildChildrenTree(permissionVO, permissionVOList)); // 递归构建children
                    return permissionVO;
                }).toList();
    }

}
