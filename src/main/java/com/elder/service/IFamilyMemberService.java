package com.elder.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elder.pojo.entity.FamilyMember;
import com.elder.pojo.query.FamilyMemberQuery;
import com.elder.pojo.vo.FamilyMemberVO;

public interface IFamilyMemberService extends IService<FamilyMember> {
    IPage<FamilyMemberVO> list(FamilyMemberQuery query);
}
