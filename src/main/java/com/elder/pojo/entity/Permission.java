package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author Gronru
 * @since 2026-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Permission implements Serializable {


    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属上级
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型(0:目录,1:菜单,2:按钮)
     */
    private Integer type;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 权限值
     */
    @TableField("permission_value")
    private String permissionValue;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(1:正常，0:禁止)
     */
    private Integer status;

    /**
     * 逻辑删除 0（true）未删除， 1（false）已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
