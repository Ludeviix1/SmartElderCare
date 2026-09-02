package com.elder.controller.admin;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 护理计划和项目关联表 前端控制器
 * </p>
 *
 * 护理项目明细跟随护理计划一起保存，统一走 /care-plan 接口，这里不单独提供增删改查
 *
 * @author Gronru
 * @since 2026-09-01
 */
@RestController
@RequestMapping("/admin/care-plan-item")
public class CarePlanItemController {

}
