package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.CareLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.CareLevelQuery;

/**
 * <p>
 * 护理等级表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
public interface ICareLevelService extends IService<CareLevel> {

    IPage<CareLevel> list(CareLevelQuery careLevelQuery);

    void add(CareLevel careLevel);
}
