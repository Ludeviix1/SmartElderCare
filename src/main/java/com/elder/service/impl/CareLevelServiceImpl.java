package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.exception.ServiceException;
import com.elder.pojo.entity.CareLevel;
import com.elder.mapper.CareLevelMapper;
import com.elder.pojo.query.CareLevelQuery;
import com.elder.service.ICareLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 护理等级表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
@Service
@Slf4j
public class CareLevelServiceImpl extends ServiceImpl<CareLevelMapper, CareLevel> implements ICareLevelService {
    @Autowired
    private CareLevelMapper careLevelMapper;

    @Override
    public IPage<CareLevel> list(CareLevelQuery careLevelQuery) {
        IPage<CareLevel> page = new Page<>(careLevelQuery.getPage(), careLevelQuery.getLimit());

        LambdaQueryWrapper<CareLevel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(careLevelQuery.getName()), CareLevel::getName, careLevelQuery.getName())
                .eq(!ObjectUtils.isEmpty(careLevelQuery.getStatus()), CareLevel::getStatus, careLevelQuery.getStatus())
                .between(!ObjectUtils.isEmpty(careLevelQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(careLevelQuery.getEndCreateTime()), CareLevel::getCreateTime, careLevelQuery.getBeginCreateTime(), careLevelQuery.getEndCreateTime())
                .orderByAsc(CareLevel::getSort);

        return careLevelMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public void add(CareLevel careLevel) {
        log.info("添加护理等级：{}", careLevel);
        CareLevel careLevelInDB = careLevelMapper.selectOne(new QueryWrapper<CareLevel>().eq("name", careLevel.getName()));
        log.info("护理等级在数据库中的信息：{}", careLevelInDB);
        if (careLevelInDB != null) {
            throw new ServiceException("护理等级名称已存在");
        }

        careLevelMapper.insert(careLevel);
    }
}
