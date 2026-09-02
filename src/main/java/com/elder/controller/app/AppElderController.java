package com.elder.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.elder.pojo.entity.Elder;
import com.elder.pojo.vo.ElderInfoVO;
import com.elder.service.IElderService;
import com.elder.util.JwtUtil;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// appElderController
@RestController
@RequestMapping("/app/elders")
public class AppElderController {
    @Autowired
    private IElderService elderService;
    
    @PostMapping("/login")
    public Result<String> login(@RequestBody Elder elder) {
        // 根据用户名查找这个用户
        Elder dbElder = elderService.getOne(new QueryWrapper<Elder>().eq("name", elder.getName()));
        if (dbElder == null) {
            return Result.error("用户名不存在");
        }
        if (!dbElder.getPassword().equalsIgnoreCase(elder.getPassword())) {
            return Result.error("密码错误");
        }
        // 登录成功后，判断用户是否被禁用
        if (dbElder.getStatus() == 0) {
            return Result.error("用户已禁用");
        }

        // 登录成功，生成token
        Map<String, Object> map = new HashMap<>();
        map.put("id", dbElder.getId());
        map.put("name", dbElder.getName());
        String token = JwtUtil.createToken(map);
        return Result.ok("登录成功", token);
    }

    /**
     * 查询当前登录老人的信息
     * GET /app/elders/elderInfo
     */
    @GetMapping("/elderInfo")
    public Result<ElderInfoVO> elderInfo(@RequestHeader("Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer id = (Integer) map.get("id");
        return Result.ok(elderService.getElderInfo(id.longValue()));
    }
}
