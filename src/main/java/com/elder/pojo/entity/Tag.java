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
 * 标签表
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Tag implements Serializable {


    /**
     * 标签ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标签编码（LIVE_ALONE/EMPTY_NEST/...）
     */
    private String code;

    /**
     * 标签名称（独居/空巢/...）
     */
    private String name;

    /**
     * 逻辑删除（0：未删除，1：已删除）
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
