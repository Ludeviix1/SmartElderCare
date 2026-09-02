package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.exception.ServiceException;
import com.elder.pojo.entity.ExamItem;
import com.elder.mapper.ExamItemMapper;
import com.elder.pojo.query.ExamItemQuery;
import com.elder.service.IExamItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 体检项目表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Service
@Slf4j
public class ExamItemServiceImpl extends ServiceImpl<ExamItemMapper, ExamItem> implements IExamItemService {
    @Autowired
    private ExamItemMapper examItemMapper;

    @Override
    public IPage<ExamItem> list(ExamItemQuery examItemQuery) {
        IPage<ExamItem> page = new Page<>(examItemQuery.getPage(), examItemQuery.getLimit());

        LambdaQueryWrapper<ExamItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(examItemQuery.getName()), ExamItem::getName, examItemQuery.getName())
                .eq(!ObjectUtils.isEmpty(examItemQuery.getStatus()), ExamItem::getStatus, examItemQuery.getStatus())
                .between(!ObjectUtils.isEmpty(examItemQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(examItemQuery.getEndCreateTime()), ExamItem::getCreateTime, examItemQuery.getBeginCreateTime(), examItemQuery.getEndCreateTime())
                .orderByAsc(ExamItem::getSort);

        return examItemMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public void add(ExamItem examItem) {
        log.info("添加体检项目：{}", examItem);
        ExamItem examItemInDB = examItemMapper.selectOne(new QueryWrapper<ExamItem>().eq("name", examItem.getName()));
        log.info("体检项目在数据库中的信息：{}", examItemInDB);
        if (examItemInDB != null) {
            throw new ServiceException("体检项目名称已存在");
        }

        examItemMapper.insert(examItem);
    }
}
