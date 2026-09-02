package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("bed")
public class Bed implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String building;
    private Integer floor;
    @TableField("room_no")
    private String roomNo;
    @TableField("bed_no")
    private String bedNo;
    @TableField("bed_type")
    private String bedType;
    @TableField("monthly_price")
    private BigDecimal monthlyPrice;
    /** 0: 空闲, 1: 已入住, 2: 停用 */
    private Integer status;
    @TableField("elder_id")
    private Long elderId;
    private String remark;
    @TableLogic
    private Integer deleted;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
