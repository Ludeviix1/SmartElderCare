package com.elder.service.impl;

import com.elder.pojo.entity.ExamPackageItem;
import com.elder.mapper.ExamPackageItemMapper;
import com.elder.service.IExamPackageItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 体检套餐项目关联表 服务实现类
 * </p>
 *
 * @author Gronru
 * @since 2026-09-02
 */
@Service
public class ExamPackageItemServiceImpl extends ServiceImpl<ExamPackageItemMapper, ExamPackageItem> implements IExamPackageItemService {

    @Override
    public List<Long> getExamItemIdsByPackageId(Long packageId) {
        return lambdaQuery()
                .eq(ExamPackageItem::getPackageId, packageId)
                .orderByAsc(ExamPackageItem::getSort)
                .list()
                .stream()
                .map(ExamPackageItem::getExamItemId)
                .toList();
    }

    @Transactional
    @Override
    public void assignExamItems(Long packageId, List<Long> examItemIds) {
        //先删除这个套餐原来的体检项目，再保存新的（同elder_tag的assignTag模式）
        lambdaUpdate()
                .eq(ExamPackageItem::getPackageId, packageId)
                .remove();

        if (ObjectUtils.isEmpty(examItemIds)) {
            return;
        }

        List<ExamPackageItem> examPackageItemList = new ArrayList<>();
        for (int i = 0; i < examItemIds.size(); i++) {
            ExamPackageItem examPackageItem = new ExamPackageItem();
            examPackageItem.setPackageId(packageId);
            examPackageItem.setExamItemId(examItemIds.get(i));
            examPackageItem.setSort(i + 1);
            examPackageItemList.add(examPackageItem);
        }
        saveBatch(examPackageItemList);
    }
}
