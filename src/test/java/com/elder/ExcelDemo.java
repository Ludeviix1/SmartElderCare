package com.elder;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelDemo {

    @Test
    public void testWrite() {
        //构建数据的集合
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setId(i);
            student.setName("excel" + i);
            list.add(student);
        }
        //设置excel文件的路径和文件的名称
        String fileName = "E:\\excel\\01.xlsx";
        EasyExcel.write(fileName, Student.class).sheet("学生信息").doWrite(list);
    }

    @Test
    public void testRead() {
        //读取文件的路径和名称
        String fileName = "E:\\excel\\01.xlsx";
        EasyExcel.read(fileName, Student.class, new ExcelListener()).sheet().doRead();
    }
}
