package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.exception.ServiceException;
import com.elder.mapper.BedMapper;
import com.elder.mapper.ElderMapper;
import com.elder.pojo.entity.Bed;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.query.BedQuery;
import com.elder.pojo.vo.BedVO;
import com.elder.service.IBedService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BedServiceImpl extends ServiceImpl<BedMapper, Bed> implements IBedService {
    private final BedMapper bedMapper;
    private final ElderMapper elderMapper;

    public BedServiceImpl(BedMapper bedMapper, ElderMapper elderMapper) {
        this.bedMapper = bedMapper;
        this.elderMapper = elderMapper;
    }

    @Override
    public IPage<BedVO> list(BedQuery query) {
        Page<Bed> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<Bed> wrapper = new LambdaQueryWrapper<Bed>()
                .like(!ObjectUtils.isEmpty(query.getBuilding()), Bed::getBuilding, query.getBuilding())
                .like(!ObjectUtils.isEmpty(query.getRoomNo()), Bed::getRoomNo, query.getRoomNo())
                .eq(query.getStatus() != null, Bed::getStatus, query.getStatus())
                .orderByAsc(Bed::getBuilding).orderByAsc(Bed::getFloor).orderByAsc(Bed::getRoomNo).orderByAsc(Bed::getBedNo);
        IPage<Bed> bedPage = bedMapper.selectPage(page, wrapper);
        List<Long> elderIds = bedPage.getRecords().stream().map(Bed::getElderId).filter(id -> id != null).toList();
        Map<Long, String> elderNames = elderIds.isEmpty() ? Map.of() : elderMapper.selectBatchIds(elderIds).stream()
                .collect(Collectors.toMap(Elder::getId, Elder::getName));
        List<BedVO> records = bedPage.getRecords().stream().map(bed -> {
            BedVO vo = new BedVO();
            BeanUtils.copyProperties(bed, vo);
            vo.setElderName(elderNames.get(bed.getElderId()));
            return vo;
        }).toList();
        Page<BedVO> result = new Page<>(bedPage.getCurrent(), bedPage.getSize(), bedPage.getTotal());
        result.setRecords(records);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignElder(Long bedId, Long elderId) {
        Bed bed = getRequiredBed(bedId);
        if (bed.getStatus() != null && bed.getStatus() == 2) {
            throw new ServiceException("停用床位不能办理入住");
        }
        Elder elder = elderMapper.selectById(elderId);
        if (elder == null) {
            throw new ServiceException("老人不存在或已删除");
        }
        Bed occupiedBed = bedMapper.selectOne(new LambdaQueryWrapper<Bed>().eq(Bed::getElderId, elderId).ne(Bed::getId, bedId));
        if (occupiedBed != null) {
            throw new ServiceException("该老人已分配床位，请先办理退床");
        }
        if (bed.getElderId() != null && !bed.getElderId().equals(elderId)) {
            throw new ServiceException("该床位已有老人入住，请先办理退床");
        }
        bed.setElderId(elderId);
        bed.setStatus(1);
        bedMapper.updateById(bed);
        elder.setBedId(bedId);
        elderMapper.updateById(elder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void releaseElder(Long bedId) {
        Bed bed = getRequiredBed(bedId);
        if (bed.getElderId() == null) {
            return;
        }
        Elder elder = elderMapper.selectById(bed.getElderId());
        if (elder != null && bedId.equals(elder.getBedId())) {
            elder.setBedId(null);
            elderMapper.updateById(elder);
        }
        bed.setElderId(null);
        bed.setStatus(0);
        bedMapper.updateById(bed);
    }

    @Override
    public void removeBed(Long id) {
        Bed bed = getRequiredBed(id);
        if (bed.getElderId() != null) {
            throw new ServiceException("已有老人入住的床位不能删除");
        }
        removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBeds(Collection<Long> ids) {
        for (Long id : ids) {
            removeBed(id);
        }
    }

    private Bed getRequiredBed(Long id) {
        Bed bed = bedMapper.selectById(id);
        if (bed == null) {
            throw new ServiceException("床位不存在或已删除");
        }
        return bed;
    }
}
