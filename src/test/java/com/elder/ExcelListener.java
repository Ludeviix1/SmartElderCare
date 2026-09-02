package com.elder;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

public class ExcelListener extends AnalysisEventListener<Student> {

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头数据：" + headMap);
    }

    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        System.out.println(student);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("所有数据解析完成！");
    }
}
