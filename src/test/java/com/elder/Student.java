package com.elder;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Student {
    //设置Excel表头名称
    @ExcelProperty(value = "学生id")
    private Integer id;
    @ExcelProperty(value = "学生姓名")
    private String name;
}
