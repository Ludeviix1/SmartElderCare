package com.elder.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.elder.mapper.UserMapper;
import com.elder.pojo.entity.User;
import com.elder.pojo.vo.UserExcelVO;
import org.springframework.beans.BeanUtils;

public class UserExcelListener extends AnalysisEventListener<UserExcelVO> {

    //Autowired members must be defined in valid Spring bean (@Component|@Service|...)
    //@Autowired
    private UserMapper userMapper;

    public UserExcelListener(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void invoke(UserExcelVO userExcelVO, AnalysisContext analysisContext) {
        User user = new User();
        BeanUtils.copyProperties(userExcelVO, user);
        user.setId(null);
        userMapper.insert(user);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
