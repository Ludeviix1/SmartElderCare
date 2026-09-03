package com.elder.tools;

import com.elder.pojo.vo.ElderInfoVO;
import com.elder.service.IElderService;
import org.springframework.ai.tool.annotation.Tool;

/** Tools exposed only for the current elder's conversation. */
public class ElderTools {

    private final Long elderId;
    private final IElderService elderService;

    public ElderTools(Long elderId, IElderService elderService) {
        this.elderId = elderId;
        this.elderService = elderService;
    }

    @Tool(description = "查询当前登录老人的基础资料。当用户询问自己的姓名、年龄、住址或联系方式时调用。")
    public ElderInfoVO getCurrentElderInfo() {
        return elderService.getElderInfo(elderId);
    }
}
