package com.elder.controller.admin;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.elder.pojo.dto.CarePlanDTO;
import com.elder.pojo.query.CarePlanQuery;
import com.elder.pojo.vo.CarePlanVO;
import com.elder.service.ICarePlanService;
import com.elder.service.IUserService;
import com.elder.util.JwtUtil;
import com.elder.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 护理计划表 前端控制器
 * </p>
 *
 * @author Gronru
 * @since 2026-09-01
 */
@RestController
@RequestMapping("/admin/care-plan")
public class CarePlanController {

    @Autowired
    private ICarePlanService carePlanService;
    @Autowired
    private IUserService userService;

    /**
     * 分页查询护理计划列表（附带老人、护理人员、护理等级名称）
     * GET /care-plan?page=1&limit=10&name=xxx
     */
    @GetMapping
    public Result<IPage<CarePlanVO>> list(CarePlanQuery carePlanQuery,
                                          @RequestHeader("Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        Long currentUserId = Long.valueOf(map.get("id").toString());
        if (userService.hasRoleCode(currentUserId, "hugong")) {
            carePlanQuery.setUserId(currentUserId);
        }
        IPage<CarePlanVO> page = carePlanService.list(carePlanQuery);
        return Result.ok(page);
    }

    /**
     * 根据ID查询护理计划及其护理项目明细
     * GET /care-plan/1
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        return Result.ok(carePlanService.selectByIdWithItems(id));
    }

    /**
     * 新增护理计划（连同护理项目明细一起保存）
     * POST /care-plan
     */
    @PostMapping
    public Result add(@RequestBody CarePlanDTO carePlanDTO) {
        carePlanService.add(carePlanDTO);
        return Result.ok("新增成功");
    }

    /**
     * 修改护理计划（护理项目明细整体替换）
     * PUT /care-plan/1
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody CarePlanDTO carePlanDTO) {
        carePlanService.update(id, carePlanDTO);
        return Result.ok("修改成功");
    }

    /**
     * 根据ID删除护理计划（连同其护理项目明细）
     * DELETE /care-plan/1
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        carePlanService.removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 批量删除护理计划（连同其护理项目明细）
     * DELETE /care-plan
     */
    @DeleteMapping
    public Result deleteBatch(@RequestBody Long[] ids) {
        carePlanService.removeByIds(java.util.Arrays.asList(ids));
        return Result.ok("批量删除成功");
    }
}
