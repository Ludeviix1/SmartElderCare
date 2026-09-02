package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 护理计划和项目关联表
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CarePlanItem implements Serializable {


    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划id
     */
    @TableField("care_plan_id")
    private Long carePlanId;

    /**
     * 项目id
     */
    @TableField("care_item_id")
    private Long careItemId;

    /**
     * 计划执行时间
     */
    @TableField("execute_time")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date executeTime;

    /**
     * 执行周期 0 天 1 周 2月
     */
    @TableField("execute_cycle")
    private Integer executeCycle;

    /**
     * 执行频次
     */
    @TableField("execute_frequency")
    private Integer executeFrequency;

    /**
     * 备注
     */
    private String remark;

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
