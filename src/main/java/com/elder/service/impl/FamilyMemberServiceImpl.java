package com.elder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elder.mapper.ElderMapper;
import com.elder.mapper.FamilyMemberMapper;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.entity.FamilyMember;
import com.elder.pojo.query.FamilyMemberQuery;
import com.elder.pojo.vo.FamilyMemberVO;
import com.elder.service.IFamilyMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements IFamilyMemberService {
    private final FamilyMemberMapper familyMemberMapper;
    private final ElderMapper elderMapper;

    public FamilyMemberServiceImpl(FamilyMemberMapper familyMemberMapper, ElderMapper elderMapper) {
        this.familyMemberMapper = familyMemberMapper;
        this.elderMapper = elderMapper;
    }

    @Override
    public IPage<FamilyMemberVO> list(FamilyMemberQuery query) {
        Page<FamilyMember> page = new Page<>(query.getPage(), query.getLimit());
        LambdaQueryWrapper<FamilyMember> wrapper = new LambdaQueryWrapper<FamilyMember>()
                .like(!ObjectUtils.isEmpty(query.getName()), FamilyMember::getName, query.getName())
                .like(!ObjectUtils.isEmpty(query.getPhone()), FamilyMember::getPhone, query.getPhone())
                .eq(query.getElderId() != null, FamilyMember::getElderId, query.getElderId())
                .orderByDesc(FamilyMember::getIsPrimary)
                .orderByDesc(FamilyMember::getCreateTime);
        IPage<FamilyMember> memberPage = familyMemberMapper.selectPage(page, wrapper);
        List<Long> elderIds = memberPage.getRecords().stream().map(FamilyMember::getElderId).distinct().toList();
        Map<Long, String> elderNames = elderIds.isEmpty() ? Map.of() : elderMapper.selectBatchIds(elderIds).stream()
                .collect(Collectors.toMap(Elder::getId, Elder::getName));
        List<FamilyMemberVO> records = memberPage.getRecords().stream().map(member -> {
            FamilyMemberVO vo = new FamilyMemberVO();
            BeanUtils.copyProperties(member, vo);
            vo.setElderName(elderNames.get(member.getElderId()));
            return vo;
        }).toList();
        Page<FamilyMemberVO> result = new Page<>(memberPage.getCurrent(), memberPage.getSize(), memberPage.getTotal());
        result.setRecords(records);
        return result;
    }
}
