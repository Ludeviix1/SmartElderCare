package com.elder.service;

import com.elder.pojo.entity.ExamPackageItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 体检套餐项目关联表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
public interface IExamPackageItemService extends IService<ExamPackageItem> {

    /**
     * 查询套餐已分配的体检项目ID列表
     */
    List<Long> getExamItemIdsByPackageId(Long packageId);

    /**
     * 给套餐分配体检项目（先删除原有关联，再保存新的，同elder_tag的assignTag模式）
     */
    void assignExamItems(Long packageId, List<Long> examItemIds);
}
