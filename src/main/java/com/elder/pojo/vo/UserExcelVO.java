package com.elder.pojo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserExcelVO {

    /**
     * 课程id
     */
    @ExcelProperty(value = "用户id")
    private Long id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String name;

    /**
     * 密码哈希
     */
    @ExcelProperty(value = "密码哈希")
    private String password;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 头像URL
     */
    @ExcelProperty(value = "头像URL")
    private String avatar;

    /**
     * 状态（0：停用，1：正常）
     */
    @ExcelProperty(value = "状态（0：停用，1：正常）")
    private Integer status;

    /**
     * 逻辑删除（0：未删除，1：已删除）
     */
    @ExcelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    private Integer deleted;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

}
