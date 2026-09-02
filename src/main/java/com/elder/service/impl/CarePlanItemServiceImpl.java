package com.elder.service.impl;

import com.elder.pojo.entity.CarePlanItem;
import com.elder.mapper.CarePlanItemMapper;
import com.elder.service.ICarePlanItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 护理计划和项目关联表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
@Service
public class CarePlanItemServiceImpl extends ServiceImpl<CarePlanItemMapper, CarePlanItem> implements ICarePlanItemService {

}
