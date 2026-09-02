package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.exception.ServiceException;
import com.elder.pojo.entity.CareItem;
import com.elder.mapper.CareItemMapper;
import com.elder.pojo.query.CareItemQuery;
import com.elder.service.ICareItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 护理项目表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-31
 */
@Service
@Slf4j
public class CareItemServiceImpl extends ServiceImpl<CareItemMapper, CareItem> implements ICareItemService {
    @Autowired
    private CareItemMapper careItemMapper;

    @Override
    public IPage<CareItem> list(CareItemQuery careItemQuery) {
        IPage<CareItem> page = new Page<>(careItemQuery.getPage(), careItemQuery.getLimit());

        LambdaQueryWrapper<CareItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(careItemQuery.getName()), CareItem::getName, careItemQuery.getName())
                .eq(!ObjectUtils.isEmpty(careItemQuery.getStatus()), CareItem::getStatus, careItemQuery.getStatus())
                .between(!ObjectUtils.isEmpty(careItemQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(careItemQuery.getEndCreateTime()), CareItem::getCreateTime, careItemQuery.getBeginCreateTime(), careItemQuery.getEndCreateTime())
                .orderByAsc(CareItem::getSort);

        return careItemMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public void add(CareItem careItem) {
        log.info("添加护理项目：{}", careItem);
        CareItem careItemInDB = careItemMapper.selectOne(new QueryWrapper<CareItem>().eq("name", careItem.getName()));
        log.info("护理项目在数据库中的信息：{}", careItemInDB);
        if (careItemInDB != null) {
            throw new ServiceException("护理项目名称已存在");
        }

        careItemMapper.insert(careItem);
    }
}
