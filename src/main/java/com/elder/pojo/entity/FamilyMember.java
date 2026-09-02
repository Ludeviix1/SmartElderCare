package com.elder.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("family_member")
public class FamilyMember implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("elder_id")
    private Long elderId;
    private String name;
    private String relation;
    private String phone;
    @TableField("id_card_no")
    private String idCardNo;
    @TableField("is_primary")
    private Integer isPrimary;
    private String address;
    private String remark;
    @TableLogic
    private Integer deleted;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
