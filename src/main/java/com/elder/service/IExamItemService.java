package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.ExamItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.ExamItemQuery;

/**
 * <p>
 * 体检项目表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
public interface IExamItemService extends IService<ExamItem> {

    IPage<ExamItem> list(ExamItemQuery examItemQuery);

    void add(ExamItem examItem);
}
