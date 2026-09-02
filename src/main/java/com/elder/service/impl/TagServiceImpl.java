package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.pojo.entity.Tag;
import com.elder.mapper.TagMapper;
import com.elder.pojo.query.TagQuery;
import com.elder.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public IPage<Tag> list(TagQuery tagQuery) {
        IPage<Tag> page = new Page<>(tagQuery.getPage(), tagQuery.getLimit());

        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(tagQuery.getName()), Tag::getName, tagQuery.getName())
                .between(!ObjectUtils.isEmpty(tagQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(tagQuery.getEndCreateTime()), Tag::getCreateTime, tagQuery.getBeginCreateTime(), tagQuery.getEndCreateTime())
                .orderByDesc(Tag::getCreateTime);

        return tagMapper.selectPage(page, lambdaQueryWrapper);
    }
}
