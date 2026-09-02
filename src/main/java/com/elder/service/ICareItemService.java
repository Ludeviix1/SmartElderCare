package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.CareItem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.CareItemQuery;

/**
 * <p>
 * 护理项目表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
public interface ICareItemService extends IService<CareItem> {

    IPage<CareItem> list(CareItemQuery careItemQuery);

    void add(CareItem careItem);
}
