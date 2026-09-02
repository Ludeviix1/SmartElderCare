package com.elder.pojo.vo;

import com.elder.pojo.entity.FamilyMember;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FamilyMemberVO extends FamilyMember {
    private String elderName;
}
