package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.elder.exception.ServiceException;
import com.elder.pojo.entity.ExamItem;
import com.elder.pojo.entity.ExamPackage;
import com.elder.pojo.entity.ExamPackageItem;
import com.elder.mapper.ExamPackageMapper;
import com.elder.pojo.query.ExamPackageQuery;
import com.elder.pojo.vo.ExamPackageDetailVO;
import com.elder.pojo.vo.ExamPackageVO;
import com.elder.service.IExamItemService;
import com.elder.service.IExamPackageItemService;
import com.elder.service.IExamPackageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 体检套餐表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Service
@Slf4j
public class ExamPackageServiceImpl extends ServiceImpl<ExamPackageMapper, ExamPackage> implements IExamPackageService {
    @Autowired
    private ExamPackageMapper examPackageMapper;
    @Autowired
    private IExamPackageItemService examPackageItemService;
    @Autowired
    private IExamItemService examItemService;

    @Override
    public IPage<ExamPackage> list(ExamPackageQuery examPackageQuery) {
        IPage<ExamPackage> page = new Page<>(examPackageQuery.getPage(), examPackageQuery.getLimit());

        LambdaQueryWrapper<ExamPackage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(!ObjectUtils.isEmpty(examPackageQuery.getName()), ExamPackage::getName, examPackageQuery.getName())
                .eq(!ObjectUtils.isEmpty(examPackageQuery.getStatus()), ExamPackage::getStatus, examPackageQuery.getStatus())
                .between(!ObjectUtils.isEmpty(examPackageQuery.getBeginCreateTime()) && !ObjectUtils.isEmpty(examPackageQuery.getEndCreateTime()), ExamPackage::getCreateTime, examPackageQuery.getBeginCreateTime(), examPackageQuery.getEndCreateTime())
                .orderByAsc(ExamPackage::getSort);

        return examPackageMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public void add(ExamPackage examPackage) {
        log.info("添加体检套餐：{}", examPackage);
        ExamPackage examPackageInDB = examPackageMapper.selectOne(new QueryWrapper<ExamPackage>().eq("name", examPackage.getName()));
        log.info("体检套餐在数据库中的信息：{}", examPackageInDB);
        if (examPackageInDB != null) {
            throw new ServiceException("体检套餐名称已存在");
        }

        examPackageMapper.insert(examPackage);
    }

    /**
     * 删除体检套餐时连带删除它的体检项目关联
     */
    @Transactional
    @Override
    public boolean removeById(Serializable id) {
        removeExamPackageItem((Long) id);
        return super.removeById(id);
    }

    /**
     * 批量删除体检套餐时连带删除它们的体检项目关联
     */
    @Transactional
    @Override
    public boolean removeByIds(Collection<?> idList) {
        if (ObjectUtils.isEmpty(idList)) {
            return false;
        }
        LambdaQueryWrapper<ExamPackageItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(ExamPackageItem::getPackageId, idList);
        examPackageItemService.remove(itemWrapper);
        return super.removeByIds(idList);
    }

    /**
     * 删除某个套餐的体检项目关联
     */
    private void removeExamPackageItem(Long packageId) {
        LambdaQueryWrapper<ExamPackageItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(ExamPackageItem::getPackageId, packageId);
        examPackageItemService.remove(itemWrapper);
    }

    @Override
    public List<ExamPackageVO> listOnShelf() {
        List<ExamPackage> examPackageList = lambdaQuery()
                .eq(ExamPackage::getStatus, 1)
                .orderByAsc(ExamPackage::getSort)
                .list();
        if (ObjectUtils.isEmpty(examPackageList)) {
            return List.of();
        }

        //批量统计每个套餐包含的体检项目数量
        List<Long> packageIdList = examPackageList.stream().map(ExamPackage::getId).toList();
        Map<Long, Long> itemCountMap = examPackageItemService.lambdaQuery()
                .in(ExamPackageItem::getPackageId, packageIdList)
                .list()
                .stream()
                .collect(Collectors.groupingBy(ExamPackageItem::getPackageId, Collectors.counting()));

        return examPackageList.stream().map(examPackage -> {
            ExamPackageVO examPackageVO = new ExamPackageVO();
            BeanUtils.copyProperties(examPackage, examPackageVO);
            examPackageVO.setItemCount(itemCountMap.getOrDefault(examPackage.getId(), 0L).intValue());
            return examPackageVO;
        }).toList();
    }

    @Override
    public ExamPackageDetailVO selectDetailById(Long id) {
        ExamPackage examPackage = getById(id);
        if (examPackage == null) {
            throw new ServiceException("体检套餐不存在");
        }
        if (examPackage.getStatus() != 1) {
            throw new ServiceException("体检套餐已下架");
        }

        ExamPackageDetailVO detailVO = new ExamPackageDetailVO();
        BeanUtils.copyProperties(examPackage, detailVO);

        //按套餐内排序查出关联的项目id
        List<ExamPackageItem> examPackageItemList = examPackageItemService.lambdaQuery()
                .eq(ExamPackageItem::getPackageId, id)
                .orderByAsc(ExamPackageItem::getSort)
                .list();
        if (ObjectUtils.isEmpty(examPackageItemList)) {
            detailVO.setExamItemList(List.of());
            return detailVO;
        }

        //批量查出项目明细，按关联顺序组装
        List<Long> examItemIdList = examPackageItemList.stream().map(ExamPackageItem::getExamItemId).toList();
        Map<Long, ExamItem> examItemMap = examItemService.listByIds(examItemIdList).stream()
                .collect(Collectors.toMap(ExamItem::getId, Function.identity()));
        List<ExamItem> examItemList = new ArrayList<>();
        for (ExamPackageItem examPackageItem : examPackageItemList) {
            ExamItem examItem = examItemMap.get(examPackageItem.getExamItemId());
            if (examItem != null) {
                examItemList.add(examItem);
            }
        }
        detailVO.setExamItemList(examItemList);
        return detailVO;
    }
}
