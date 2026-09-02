package com.elder.pojo.vo;

import com.elder.pojo.entity.Bed;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BedVO extends Bed {
    private String elderName;
}
