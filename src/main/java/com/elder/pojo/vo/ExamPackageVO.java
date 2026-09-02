package com.elder.pojo.vo;

import com.elder.pojo.entity.ExamPackage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 体检套餐列表VO（老人手机端）：在套餐信息基础上附带包含的项目数量
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExamPackageVO extends ExamPackage {

    /**
     * 包含的体检项目数量
     */
    private Integer itemCount;

}
