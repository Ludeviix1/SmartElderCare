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
 * 体检套餐表
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamPackage implements Serializable {


    /**
     * 体检套餐ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐价格
     */
    private BigDecimal price;

    /**
     * 套餐图片
     */
    private String image;

    /**
     * 套餐说明
     */
    private String description;

    /**
     * 状态：0下架 1上架
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
