package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.query.TagQuery;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
public interface ITagService extends IService<Tag> {

    IPage<Tag> list(TagQuery tagQuery);
}
