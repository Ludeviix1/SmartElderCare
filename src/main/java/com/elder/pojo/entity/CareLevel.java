package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 护理等级表
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CareLevel implements Serializable {


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 护理费用
     */
    private BigDecimal price;

    /**
     * 等级说明
     */
    private String description;

    /**
     * 状态：0禁用 1启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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
