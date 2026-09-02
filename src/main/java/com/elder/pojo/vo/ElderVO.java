package com.elder.pojo.vo;

import com.elder.pojo.entity.Elder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 老人列表VO：在老人信息基础上附带已分配的标签名
 *
 * @author Gronru
 * @since 2026-08-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ElderVO extends Elder {

    /**
     * 已分配的标签名称列表
     */
    private List<String> tagNames;

}
