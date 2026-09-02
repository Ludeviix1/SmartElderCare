package com.elder.pojo.vo;

import com.elder.pojo.entity.CareTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 护理任务列表VO：在护理任务信息基础上附带老人、护理员、护理计划名称
 *
 * @author Gronru
 * @since 2026-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CareTaskVO extends CareTask {

    /**
     * 老人姓名
     */
    private String elderName;

    /**
     * 护理员姓名
     */
    private String userName;

    /**
     * 护理计划名称
     */
    private String carePlanName;

}
