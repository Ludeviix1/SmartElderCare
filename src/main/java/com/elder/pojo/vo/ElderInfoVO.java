package com.elder.pojo.vo;

import lombok.Data;

/**
 * 老人手机端个人信息VO：从elder表整理，age由birthday计算得出
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
public class ElderInfoVO {

    /**
     * 老人ID
     */
    private Long id;

    /**
     * 老人姓名
     */
    private String name;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 年龄（由出生日期计算）
     */
    private Integer age;

    /**
     * 家庭住址
     */
    private String address;

    /**
     * 出生日期（yyyy-MM-dd）
     */
    private String birthday;

}
