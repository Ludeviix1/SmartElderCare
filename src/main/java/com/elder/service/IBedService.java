package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.entity.Bed;
import com.elder.pojo.query.BedQuery;
import com.elder.pojo.vo.BedVO;

import java.util.Collection;

public interface IBedService extends IService<Bed> {
    IPage<BedVO> list(BedQuery query);
    void assignElder(Long bedId, Long elderId);
    void releaseElder(Long bedId);
    void removeBed(Long id);
    void removeBeds(Collection<Long> ids);
}
