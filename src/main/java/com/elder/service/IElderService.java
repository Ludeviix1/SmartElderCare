package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.query.ElderQuery;
import com.elder.pojo.vo.ElderInfoVO;
import com.elder.pojo.vo.ElderVO;

import java.util.Map;

/**
 * <p>
 * 老人表 服务类
 * </p>
 *
 * @author Gronru
 * @since 2026-08-26
 */
public interface IElderService extends IService<Elder> {

    IPage<ElderVO> list(ElderQuery elderQuery);

    Map<String, Object> selectAssignedTag(Long elderId);

    void assignTag(Long elderId, Long[] tagIds);

    /**
     * 查询老人手机端个人信息（age由birthday计算）
     */
    ElderInfoVO getElderInfo(Long elderId);
}
