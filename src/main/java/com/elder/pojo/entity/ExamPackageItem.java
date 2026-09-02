package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 体检套餐项目关联表
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamPackageItem implements Serializable {


    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 体检套餐ID
     */
    @TableField("package_id")
    private Long packageId;

    /**
     * 体检项目ID
     */
    @TableField("exam_item_id")
    private Long examItemId;

    /**
     * 项目排序
     */
    private Integer sort;

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
