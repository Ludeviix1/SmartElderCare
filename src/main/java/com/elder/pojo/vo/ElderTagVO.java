package com.elder.pojo.vo;

import com.elder.pojo.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ElderTagVO {
    private List<Tag> tagList;
    private List<Long> assignedTagIdList;
}
