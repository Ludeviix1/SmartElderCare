package com.elder.pojo.vo;

import com.elder.pojo.entity.Permission;
import lombok.Data;

import java.util.List;

@Data
public class PermissionVO extends Permission {
    private List<PermissionVO> children;
}
