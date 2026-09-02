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
 * 体检项目表
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamItem implements Serializable {


    /**
     * 体检项目ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 单项价格
     */
    private BigDecimal price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 结果类型：0文本 1数值
     */
    @TableField("result_type")
    private Integer resultType;

    /**
     * 参考范围下限
     */
    @TableField("reference_min")
    private BigDecimal referenceMin;

    /**
     * 参考范围上限
     */
    @TableField("reference_max")
    private BigDecimal referenceMax;

    /**
     * 参考范围单位
     */
    @TableField("reference_unit")
    private String referenceUnit;

    /**
     * 项目说明
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
