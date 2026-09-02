package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.entity.CareTask;
import com.elder.pojo.query.CareTaskQuery;
import com.elder.pojo.vo.CareTaskVO;

/**
 * <p>
 * 护理任务与打卡记录表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
public interface ICareTaskService extends IService<CareTask> {

    IPage<CareTaskVO> list(CareTaskQuery careTaskQuery);
}
