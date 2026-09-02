package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.entity.CarePlan;
import com.elder.pojo.dto.CarePlanDTO;
import com.elder.pojo.query.CarePlanQuery;
import com.elder.pojo.vo.CarePlanVO;

/**
 * <p>
 * 护理计划表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
public interface ICarePlanService extends IService<CarePlan> {

    IPage<CarePlanVO> list(CarePlanQuery carePlanQuery);

    void add(CarePlanDTO carePlanDTO);

    void update(Long id, CarePlanDTO carePlanDTO);

    /**
     * 根据ID查询护理计划及其护理项目明细
     */
    CarePlanDTO selectByIdWithItems(Long id);
}
