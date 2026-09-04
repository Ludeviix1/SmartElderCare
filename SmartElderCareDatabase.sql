SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bed
-- ----------------------------
DROP TABLE IF EXISTS `bed`;
CREATE TABLE `bed`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `building` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '楼栋',
  `floor` int NOT NULL COMMENT '楼层',
  `room_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '房间号',
  `bed_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '床位号',
  `bed_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '床位类型',
  `monthly_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '月费',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0空闲，1已入住，2停用',
  `elder_id` bigint NULL DEFAULT NULL COMMENT '当前入住老人ID',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bed_location`(`building` ASC, `floor` ASC, `room_no` ASC, `bed_no` ASC) USING BTREE,
  UNIQUE INDEX `uk_bed_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_bed_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '床位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bed
-- ----------------------------
INSERT INTO `bed` VALUES (1, '1号楼', 1, '101', '01', '单人护理床', 3200.00, 1, 22, '靠窗', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (2, '1号楼', 1, '101', '02', '单人护理床', 3200.00, 1, 23, '靠窗', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (3, '1号楼', 1, '101', '03', '单人护理床', 3200.00, 1, 21, NULL, 0, '2026-09-02 19:59:52', '2026-09-03 08:38:19');
INSERT INTO `bed` VALUES (4, '1号楼', 1, '102', '01', '双人护理床', 2800.00, 2, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-03 17:01:17');
INSERT INTO `bed` VALUES (5, '1号楼', 1, '102', '02', '双人护理床', 2800.00, 2, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-03 17:01:22');
INSERT INTO `bed` VALUES (6, '1号楼', 1, '102', '03', '双人护理床', 2800.00, 2, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-03 17:01:27');
INSERT INTO `bed` VALUES (7, '1号楼', 2, '201', '01', '单人护理床', 3500.00, 1, 4, '带独立卫生间', 0, '2026-09-02 19:59:52', '2026-09-03 17:01:34');
INSERT INTO `bed` VALUES (8, '1号楼', 2, '201', '02', '单人护理床', 3500.00, 0, NULL, '带独立卫生间', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (9, '1号楼', 2, '201', '03', '单人护理床', 3500.00, 0, NULL, '带独立卫生间', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (10, '1号楼', 2, '202', '01', '双人护理床', 3000.00, 0, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (11, '1号楼', 2, '202', '02', '双人护理床', 3000.00, 0, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `bed` VALUES (12, '1号楼', 2, '202', '03', '双人护理床', 3000.00, 0, NULL, NULL, 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');

-- ----------------------------
-- Table structure for care_item
-- ----------------------------
DROP TABLE IF EXISTS `care_item`;
CREATE TABLE `care_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单次服务价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `requirement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '护理要求',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int NOT NULL DEFAULT 0 COMMENT '逻辑删除：0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '护理项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_item
-- ----------------------------
INSERT INTO `care_item` VALUES (10, '测量血压', 10.00, NULL, '协助老人测量血压，并记录测量结果', 1, 1, '2026-08-29 16:01:51', '2026-08-31 17:19:21', 0);
INSERT INTO `care_item` VALUES (11, '测量血糖', 15.00, NULL, '根据护理计划协助老人测量血糖，并记录测量结果', 2, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (12, '测量体温', 5.00, NULL, '协助老人测量体温，并记录测量结果', 3, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (13, '协助吃饭', 20.00, NULL, '协助老人进餐，关注进食情况，必要时提供喂饭服务', 4, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (14, '协助洗澡', 50.00, NULL, '协助老人完成洗澡，注意防滑、防跌倒，保障老人安全', 5, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (15, '协助如厕', 20.00, NULL, '协助老人安全如厕，做好必要的清洁和卫生护理', 6, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (16, '协助起床', 15.00, NULL, '协助老人安全起床，注意防止跌倒和意外发生', 7, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (17, '协助服药', 10.00, NULL, '按照医嘱和用药计划提醒并协助老人服药，不得擅自调整药物剂量', 8, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (18, '康复训练', 50.00, NULL, '根据老人身体状况和康复计划协助开展康复训练', 9, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (19, '心理陪护', 30.00, NULL, '陪伴老人交流，关注老人情绪和心理状态', 10, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);
INSERT INTO `care_item` VALUES (20, '房间清洁', 30.00, NULL, '负责老人房间日常清洁，保持房间整洁卫生', 11, 1, '2026-08-29 16:01:51', '2026-08-29 16:01:51', 0);

-- ----------------------------
-- Table structure for care_level
-- ----------------------------
DROP TABLE IF EXISTS `care_level`;
CREATE TABLE `care_level`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '等级名称',
  `price` decimal(10, 2) NOT NULL COMMENT '护理费用',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '等级说明',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '护理等级表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_level
-- ----------------------------
INSERT INTO `care_level` VALUES (1, '自理护理', 1000.00, '老人生活能够基本自理，仅需提供日常生活服务、健康监测和基础照护', 1, 1, '2026-08-29 16:15:50', '2026-08-29 16:15:50');
INSERT INTO `care_level` VALUES (2, '一级护理', 2000.00, '老人部分生活需要协助，需要提供较为频繁的生活照护和健康监测', 1, 2, '2026-08-29 16:15:50', '2026-08-29 16:15:50');
INSERT INTO `care_level` VALUES (3, '二级护理', 3000.00, '老人生活自理能力较弱，需要提供较全面的生活照护、健康监测和康复服务', 1, 3, '2026-08-29 16:15:50', '2026-08-29 16:15:50');
INSERT INTO `care_level` VALUES (4, '三级护理', 4000.00, '老人生活自理能力较差，需要较高频次的生活照护、健康监测和专人护理', 1, 4, '2026-08-29 16:15:50', '2026-08-29 16:15:50');

-- ----------------------------
-- Table structure for care_plan
-- ----------------------------
DROP TABLE IF EXISTS `care_plan`;
CREATE TABLE `care_plan`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `user_id` bigint NOT NULL COMMENT '护理人员ID',
  `care_level_id` bigint NOT NULL COMMENT '护理等级ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '计划名称',
  `start_date` date NOT NULL,
  `end_date` date NULL DEFAULT NULL,
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 0结束 1开始',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '护理计划表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_plan
-- ----------------------------
INSERT INTO `care_plan` VALUES (1, 1, 991, 1, '张三的夏季护理', '2026-08-12', '2026-08-27', 1, '2026-08-29 16:39:27', '2026-09-02 09:48:34');
INSERT INTO `care_plan` VALUES (2, 1, 613, 2, '张三的一级护理', '2026-08-11', '2026-08-31', 1, '2026-08-29 16:54:49', '2026-09-02 09:48:29');
INSERT INTO `care_plan` VALUES (3, 3, 991, 2, '日常护理', '2026-08-10', '2026-08-28', 0, '2026-08-29 18:18:13', '2026-09-02 09:48:23');
INSERT INTO `care_plan` VALUES (4, 2, 991, 4, '日常护理', '2026-08-12', '2026-08-29', 1, '2026-08-29 23:21:06', '2026-09-02 09:48:19');
INSERT INTO `care_plan` VALUES (5, 2, 613, 2, '日常护理', '2026-08-12', '2026-08-29', 1, '2026-08-29 23:21:54', '2026-09-02 09:48:15');
INSERT INTO `care_plan` VALUES (6, 1, 613, 2, '日常护理', '2026-08-18', '2026-08-27', 1, '2026-08-30 10:16:03', '2026-09-02 09:48:09');
INSERT INTO `care_plan` VALUES (8, 4, 613, 2, '日常护理', '2026-09-03', '2026-09-03', 1, '2026-09-03 20:40:17', '2026-09-03 20:40:17');

-- ----------------------------
-- Table structure for care_plan_item
-- ----------------------------
DROP TABLE IF EXISTS `care_plan_item`;
CREATE TABLE `care_plan_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `care_plan_id` bigint NOT NULL COMMENT '计划id',
  `care_item_id` bigint NOT NULL COMMENT '项目id',
  `execute_time` time NOT NULL COMMENT '计划执行时间',
  `execute_cycle` tinyint NOT NULL COMMENT '执行周期 0 天 1 周 2月',
  `execute_frequency` tinyint NOT NULL COMMENT '执行频次',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '护理计划和项目关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_plan_item
-- ----------------------------
INSERT INTO `care_plan_item` VALUES (5, 2, 10, '16:53:00', 0, 1, NULL, '2026-09-02 09:48:29', '2026-09-02 09:48:29');
INSERT INTO `care_plan_item` VALUES (6, 2, 13, '06:52:00', 1, 3, NULL, '2026-09-02 09:48:29', '2026-09-02 09:48:29');
INSERT INTO `care_plan_item` VALUES (8, 3, 15, '18:17:00', 0, 1, NULL, '2026-09-02 09:48:23', '2026-09-02 09:48:23');
INSERT INTO `care_plan_item` VALUES (9, 4, 16, '23:20:00', 0, 1, NULL, '2026-09-02 09:48:19', '2026-09-02 09:48:19');
INSERT INTO `care_plan_item` VALUES (10, 5, 16, '23:21:00', 1, 1, NULL, '2026-09-02 09:48:15', '2026-09-02 09:48:15');
INSERT INTO `care_plan_item` VALUES (11, 6, 15, '10:15:00', 0, 1, NULL, '2026-09-02 09:48:09', '2026-09-02 09:48:09');
INSERT INTO `care_plan_item` VALUES (12, 6, 16, '10:15:00', 0, 1, NULL, '2026-09-02 09:48:09', '2026-09-02 09:48:09');
INSERT INTO `care_plan_item` VALUES (14, 8, 13, '20:39:00', 0, 1, NULL, '2026-09-03 20:40:17', '2026-09-03 20:40:17');
INSERT INTO `care_plan_item` VALUES (15, 8, 17, '20:40:00', 0, 1, NULL, '2026-09-03 20:40:17', '2026-09-03 20:40:17');

-- ----------------------------
-- Table structure for care_task
-- ----------------------------
DROP TABLE IF EXISTS `care_task`;
CREATE TABLE `care_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `care_plan_id` bigint NOT NULL COMMENT '来源护理计划ID',
  `care_item_id` bigint NOT NULL COMMENT '护理项目ID',
  `care_item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '护理项目名称(冗余，防止项目改名历史记录变动)',
  `user_id` bigint NULL DEFAULT NULL COMMENT '指定执行护理员ID/实际执行护理员ID',
  `plan_execute_date` date NOT NULL COMMENT '计划执行日期(如: 2026-08-29)',
  `plan_execute_time` time NOT NULL COMMENT '计划执行时间(如: 08:00:00)',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '任务状态（0：待执行，1：已完成，2：已跳过/取消）',
  `actual_execute_time` datetime NULL DEFAULT NULL COMMENT '实际完成时间',
  `execute_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '执行结果描述/健康数值(如: \"血压 120/80 mmHg\" 或 \"吃药完成\")',
  `execute_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '现场打卡照片URL(多张以逗号隔开)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '护理员执行备注(如: \"老人精神状态一般\")',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '任务生成时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '护理任务与打卡记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_task
-- ----------------------------
INSERT INTO `care_task` VALUES (204, 1, 6, 15, '协助如厕', 2, '2026-08-18', '10:15:00', 1, '2026-08-18 00:00:00', NULL, NULL, NULL, '2026-08-30 10:16:03', '2026-09-03 08:39:17');
INSERT INTO `care_task` VALUES (205, 1, 6, 16, '协助起床', 2, '2026-08-18', '10:15:00', 1, '2026-08-12 00:00:00', NULL, NULL, NULL, '2026-08-30 10:16:03', '2026-08-30 10:22:37');
INSERT INTO `care_task` VALUES (208, 1, 6, 15, '协助如厕', 613, '2026-08-18', '10:15:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:09', '2026-09-02 09:48:09');
INSERT INTO `care_task` VALUES (209, 1, 6, 16, '协助起床', 613, '2026-08-18', '10:15:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:09', '2026-09-02 09:48:09');
INSERT INTO `care_task` VALUES (210, 2, 5, 16, '协助起床', 613, '2026-08-12', '23:21:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:15', '2026-09-02 09:48:15');
INSERT INTO `care_task` VALUES (211, 2, 4, 16, '协助起床', 991, '2026-08-12', '23:20:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:19', '2026-09-02 09:48:19');
INSERT INTO `care_task` VALUES (212, 3, 3, 15, '协助如厕', 991, '2026-08-10', '18:17:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:23', '2026-09-02 09:48:23');
INSERT INTO `care_task` VALUES (213, 1, 2, 10, '测量血压', 613, '2026-08-11', '16:53:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:29', '2026-09-02 09:48:29');
INSERT INTO `care_task` VALUES (214, 1, 2, 13, '协助吃饭', 613, '2026-08-11', '06:52:00', 0, NULL, NULL, NULL, NULL, '2026-09-02 09:48:29', '2026-09-02 09:48:29');
INSERT INTO `care_task` VALUES (215, 4, 8, 13, '协助吃饭', 613, '2026-09-03', '20:39:00', 1, NULL, '行动不便', NULL, NULL, '2026-09-03 20:40:17', '2026-09-03 20:40:39');
INSERT INTO `care_task` VALUES (216, 4, 8, 17, '协助服药', 613, '2026-09-03', '20:40:00', 0, NULL, NULL, NULL, NULL, '2026-09-03 20:40:17', '2026-09-03 20:40:17');

-- ----------------------------
-- Table structure for elder
-- ----------------------------
DROP TABLE IF EXISTS `elder`;
CREATE TABLE `elder`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '老人ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '老人姓名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `id_card_no` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0：禁用，1：启用，2：请假，3：退住中，4：入住中，5：已退住）',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔）',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `bed_id` bigint NULL DEFAULT NULL COMMENT '当前入住床位ID',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_elder_id_card_no`(`id_card_no` ASC) USING BTREE,
  UNIQUE INDEX `uk_elder_bed_id`(`bed_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '老人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elder
-- ----------------------------
INSERT INTO `elder` VALUES (1, '张桂兰', '123456', NULL, '110101193502120011', 2, '13800138001', '1935-02-12', '北京市东城区朝阳门街道23号', 'LIVE_ALONE,C_HBP', '高血压需每日服药', NULL, 0, '2026-08-26 09:07:13', '2026-08-27 14:23:12');
INSERT INTO `elder` VALUES (2, '李德福', '123456', NULL, '110101193805180023', 4, '13800138002', '1938-05-18', '北京市西城区月坛街道8号楼', 'EMPTY_NEST,C_DM', '糖尿病II型', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (3, '王秀英', '123456', NULL, '110101194008130034', 1, '13800138003', '1940-08-13', '北京市朝阳区三里屯街道15号', 'LIVE_ALONE', '独居老人，需关注', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (4, '刘振辉', '123456', NULL, '110101194203250045', 4, '13800138004', '1942-03-24', '北京市海淀区中关村南大街6号', 'AGE_80,C_HBP,C_CHD', '高龄，有高血压和冠心病', 7, 0, '2026-08-26 09:07:13', '2026-09-03 17:01:34');
INSERT INTO `elder` VALUES (5, '陈桂芳', '123456', NULL, '110101193612100056', 1, '13800138005', '1936-12-10', '北京市丰台区方庄街道12号楼', 'DISABLED,C_DM,AGE_80', '失能，需全天护理', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:12:43');
INSERT INTO `elder` VALUES (6, '赵国强', '123456', NULL, '110101194507060067', 4, '13800138006', '1945-07-06', '北京市通州区梨园镇9号', 'EMPTY_NEST', '子女在外地', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (7, '孙淑珍', '123456', NULL, '110101193910210078', 5, '13800138007', '1939-10-21', '北京市石景山区八角街道3号', 'AGE_80', '已退住，子女接回', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:13:22');
INSERT INTO `elder` VALUES (8, '周文斌', '123456', NULL, '110101194309150089', 4, '13800138008', '1943-09-15', '北京市大兴区黄村镇西大街7号', 'LIVE_ALONE,C_HBP,C_DM', '高血压糖尿病，需饮食控制', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (9, '吴凤英', '123456', NULL, '110101194106300090', 1, '13800138009', '1941-06-30', '北京市昌平区回龙观镇28号', 'EMPTY_NEST,C_CHD', '冠心病术后恢复中', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (10, '郑明辉', '123456', NULL, '110101194702220101', 4, '13800138010', '1947-02-22', '北京市顺义区胜利街道5号', '', '身体状况良好', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (11, '马金凤', '123456', NULL, '110101193704080112', 3, '13800138011', '1937-04-08', '北京市房山区良乡街道11号', 'AGE_80,DISABLED', '高龄失能，退住办理中', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (12, '黄志远', '123456', NULL, '110101194411190123', 4, '13800138012', '1944-11-19', '北京市东城区安定门外大街66号', 'LIVE_ALONE', '性格孤僻，需多沟通', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (13, '朱玉兰', '123456', NULL, '110101194605270134', 1, '13800138013', '1946-05-27', '北京市西城区德胜门外大街18号', 'EMPTY_NEST,C_HBP', '空巢老人，轻度高血压', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (14, '何建国', '123456', NULL, '110101194001160145', 4, '13800138014', '1940-01-16', '北京市朝阳区望京街道21号', 'C_DM,C_CHD', '糖尿病合并冠心病', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (15, '高美玲', '123456', NULL, '110101193808240156', 0, '13800138015', '1938-08-24', '北京市海淀区五道口街道9号', 'LIVE_ALONE,C_DM', '已禁用，转院治疗', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (16, '林志强', '123456', NULL, '110101194212050167', 4, '13800138016', '1942-12-05', '北京市丰台区马家堡街道14号', 'EMPTY_NEST,AGE_80', '高龄空巢，需定期探访', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (17, '罗秀梅', '123456', NULL, '110101194406180178', 1, '13800138017', '1944-06-18', '北京市通州区新华街道32号', 'DISABLED,C_HBP', '失能卧床，高血压', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (18, '梁桂芝', '123456', NULL, '110101193911290189', 4, '13800138018', '1939-11-29', '北京市石景山区古城街道8号', 'LIVE_ALONE,AGE_80,C_DM', '高龄独居，糖尿病', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (19, '宋德才', '123456', NULL, '110101194608030190', 4, '13800138019', '1946-08-03', '北京市大兴区亦庄镇文化路5号', '', '身体状况一般，定期体检', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (20, '谢桂珍', '123456', NULL, '110101194103070201', 2, '13800138020', '1941-03-07', '北京市昌平区天通苑街道45号', 'EMPTY_NEST,C_CHD', '请假回老家探亲', NULL, 0, '2026-08-26 09:07:13', '2026-08-26 09:07:13');
INSERT INTO `elder` VALUES (21, '李宝珍', '123', NULL, '110102193805185314', 4, '14444444444', '1938-05-18', '北京市西城区月坛街道8号楼', NULL, '暂无', 3, 0, '2026-08-27 15:04:16', '2026-09-03 19:52:38');
INSERT INTO `elder` VALUES (22, '张建国', '123456', NULL, '110101194801011234', 4, '13800001001', '1948-01-01', '北京市朝阳区幸福路18号', NULL, '演示老人数据', 1, 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `elder` VALUES (23, '李秀兰', '123456', NULL, '110101195203082345', 4, '13800001002', '1952-03-08', '北京市海淀区康乐街26号', NULL, '演示老人数据', 2, 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');

-- ----------------------------
-- Table structure for elder_tag
-- ----------------------------
DROP TABLE IF EXISTS `elder_tag`;
CREATE TABLE `elder_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_elder_tag`(`elder_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '老人-标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of elder_tag
-- ----------------------------
INSERT INTO `elder_tag` VALUES (3, 10, 4, '2026-08-26 16:26:13', '2026-08-26 16:26:13');
INSERT INTO `elder_tag` VALUES (4, 10, 3, '2026-08-26 16:26:13', '2026-08-26 16:26:13');
INSERT INTO `elder_tag` VALUES (18, 5, 4, '2026-08-27 14:23:18', '2026-08-27 14:23:18');
INSERT INTO `elder_tag` VALUES (19, 5, 3, '2026-08-27 14:23:18', '2026-08-27 14:23:18');
INSERT INTO `elder_tag` VALUES (20, 1, 1, '2026-08-27 14:23:30', '2026-08-27 14:23:30');
INSERT INTO `elder_tag` VALUES (21, 1, 2, '2026-08-27 14:23:30', '2026-08-27 14:23:30');
INSERT INTO `elder_tag` VALUES (22, 1, 5, '2026-08-27 14:23:30', '2026-08-27 14:23:30');
INSERT INTO `elder_tag` VALUES (23, 2, 6, '2026-08-27 14:23:37', '2026-08-27 14:23:37');
INSERT INTO `elder_tag` VALUES (24, 3, 1, '2026-08-27 14:23:46', '2026-08-27 14:23:46');
INSERT INTO `elder_tag` VALUES (25, 3, 2, '2026-08-27 14:23:46', '2026-08-27 14:23:46');
INSERT INTO `elder_tag` VALUES (27, 4, 3, '2026-08-27 14:23:57', '2026-08-27 14:23:57');
INSERT INTO `elder_tag` VALUES (28, 4, 7, '2026-08-27 14:23:57', '2026-08-27 14:23:57');
INSERT INTO `elder_tag` VALUES (29, 4, 5, '2026-08-27 14:23:57', '2026-08-27 14:23:57');
INSERT INTO `elder_tag` VALUES (30, 6, 2, '2026-08-27 14:24:00', '2026-08-27 14:24:00');
INSERT INTO `elder_tag` VALUES (31, 7, 9, '2026-08-27 14:24:05', '2026-08-27 14:24:05');
INSERT INTO `elder_tag` VALUES (32, 7, 8, '2026-08-27 14:24:05', '2026-08-27 14:24:05');
INSERT INTO `elder_tag` VALUES (33, 8, 5, '2026-08-27 14:48:37', '2026-08-27 14:48:37');
INSERT INTO `elder_tag` VALUES (34, 8, 6, '2026-08-27 14:48:37', '2026-08-27 14:48:37');
INSERT INTO `elder_tag` VALUES (35, 9, 7, '2026-08-27 14:48:43', '2026-08-27 14:48:43');
INSERT INTO `elder_tag` VALUES (36, 11, 3, '2026-08-27 15:00:19', '2026-08-27 15:00:19');
INSERT INTO `elder_tag` VALUES (37, 11, 4, '2026-08-27 15:00:19', '2026-08-27 15:00:19');
INSERT INTO `elder_tag` VALUES (38, 12, 8, '2026-08-27 15:00:24', '2026-08-27 15:00:24');
INSERT INTO `elder_tag` VALUES (39, 12, 9, '2026-08-27 15:00:24', '2026-08-27 15:00:24');
INSERT INTO `elder_tag` VALUES (40, 13, 5, '2026-08-27 15:00:31', '2026-08-27 15:00:31');
INSERT INTO `elder_tag` VALUES (41, 13, 2, '2026-08-27 15:00:31', '2026-08-27 15:00:31');
INSERT INTO `elder_tag` VALUES (42, 14, 6, '2026-08-27 15:00:37', '2026-08-27 15:00:37');
INSERT INTO `elder_tag` VALUES (43, 14, 7, '2026-08-27 15:00:37', '2026-08-27 15:00:37');
INSERT INTO `elder_tag` VALUES (44, 15, 8, '2026-08-27 15:00:42', '2026-08-27 15:00:42');
INSERT INTO `elder_tag` VALUES (45, 15, 9, '2026-08-27 15:00:42', '2026-08-27 15:00:42');
INSERT INTO `elder_tag` VALUES (48, 17, 4, '2026-08-27 15:00:51', '2026-08-27 15:00:51');
INSERT INTO `elder_tag` VALUES (49, 17, 5, '2026-08-27 15:00:51', '2026-08-27 15:00:51');
INSERT INTO `elder_tag` VALUES (50, 16, 2, '2026-08-27 15:00:55', '2026-08-27 15:00:55');
INSERT INTO `elder_tag` VALUES (51, 16, 3, '2026-08-27 15:00:55', '2026-08-27 15:00:55');
INSERT INTO `elder_tag` VALUES (52, 18, 1, '2026-08-27 15:01:02', '2026-08-27 15:01:02');
INSERT INTO `elder_tag` VALUES (53, 18, 3, '2026-08-27 15:01:02', '2026-08-27 15:01:02');
INSERT INTO `elder_tag` VALUES (54, 18, 6, '2026-08-27 15:01:02', '2026-08-27 15:01:02');
INSERT INTO `elder_tag` VALUES (55, 19, 8, '2026-08-27 15:01:07', '2026-08-27 15:01:07');
INSERT INTO `elder_tag` VALUES (56, 19, 9, '2026-08-27 15:01:07', '2026-08-27 15:01:07');
INSERT INTO `elder_tag` VALUES (57, 20, 4, '2026-08-27 15:01:14', '2026-08-27 15:01:14');
INSERT INTO `elder_tag` VALUES (65, 21, 5, '2026-09-03 15:16:59', '2026-09-03 15:16:59');
INSERT INTO `elder_tag` VALUES (66, 21, 6, '2026-09-03 15:16:59', '2026-09-03 15:16:59');

-- ----------------------------
-- Table structure for exam_appointment
-- ----------------------------
DROP TABLE IF EXISTS `exam_appointment`;
CREATE TABLE `exam_appointment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '体检记录ID',
  `elder_id` bigint NOT NULL COMMENT '老人ID',
  `package_id` bigint NOT NULL COMMENT '体检套餐ID',
  `appointment_date` date NOT NULL COMMENT '预约/体检日期',
  `appointment_time` time NOT NULL COMMENT '预约/体检时间',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '体检套餐价格',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0待体检 1体检中 2已完成 3已取消 4已过期',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `caregiver_id` bigint NULL DEFAULT NULL COMMENT '负责护工ID',
  `assignment_status` tinyint NOT NULL DEFAULT 0 COMMENT '0未分配 1已分配',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_exam_appointment_caregiver`(`caregiver_id` ASC) USING BTREE,
  INDEX `idx_exam_appointment_assignment`(`assignment_status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '老人预约/体检记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_appointment
-- ----------------------------
INSERT INTO `exam_appointment` VALUES (1, 1, 2, '2026-09-05', '08:30:00', 399.00, 0, '需要工作人员陪同', '2026-08-30 16:14:48', '2026-09-03 10:19:06', 860, 1);
INSERT INTO `exam_appointment` VALUES (2, 2, 1, '2026-09-06', '09:00:00', 199.00, 0, NULL, '2026-08-30 16:14:48', '2026-09-03 10:19:05', 860, 1);
INSERT INTO `exam_appointment` VALUES (3, 3, 3, '2026-08-25', '08:00:00', 499.00, 2, '已完成体检', '2026-08-30 16:14:48', '2026-09-03 10:19:12', 502, 1);
INSERT INTO `exam_appointment` VALUES (4, 4, 4, '2026-09-08', '10:00:00', 299.00, 2, '健康，但是需要协助行动', '2026-08-30 16:14:48', '2026-09-03 20:37:49', 749, 1);
INSERT INTO `exam_appointment` VALUES (5, 5, 5, '2026-08-20', '08:30:00', 699.00, 2, '体检已完成', '2026-08-30 16:14:48', '2026-09-03 09:44:35', 502, 1);
INSERT INTO `exam_appointment` VALUES (6, 1, 1, '2026-09-01', '00:00:03', 199.00, 0, NULL, '2026-08-31 18:55:36', '2026-09-03 10:19:11', 502, 1);
INSERT INTO `exam_appointment` VALUES (7, 1, 11, '2026-09-02', '02:00:00', 3.00, 0, NULL, '2026-09-01 22:48:01', '2026-09-03 10:19:10', 860, 1);
INSERT INTO `exam_appointment` VALUES (8, 1, 4, '2026-09-03', '08:00:00', 299.00, 0, NULL, '2026-09-02 00:03:10', '2026-09-03 10:19:10', 749, 1);
INSERT INTO `exam_appointment` VALUES (9, 21, 1, '2026-09-04', '08:00:00', 199.00, 3, NULL, '2026-09-03 09:16:48', '2026-09-03 10:19:09', 860, 1);
INSERT INTO `exam_appointment` VALUES (10, 22, 5, '2026-09-04', '09:39:00', 699.00, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 10:19:07', 749, 1);
INSERT INTO `exam_appointment` VALUES (11, 21, 3, '2026-09-04', '09:39:00', 499.00, 3, NULL, '2026-09-03 09:39:48', '2026-09-03 11:00:03', 991, 1);
INSERT INTO `exam_appointment` VALUES (12, 20, 1, '2026-09-11', '08:43:00', 199.00, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:45', 502, 1);
INSERT INTO `exam_appointment` VALUES (13, 21, 11, '2026-09-04', '08:00:00', 3.00, 2, '', '2026-09-03 11:02:02', '2026-09-03 14:15:54', 991, 1);
INSERT INTO `exam_appointment` VALUES (14, 21, 5, '2026-09-05', '08:00:00', 699.00, 3, NULL, '2026-09-03 11:44:12', '2026-09-03 13:58:43', 502, 1);
INSERT INTO `exam_appointment` VALUES (15, 21, 1, '2026-09-04', '08:00:00', 199.00, 3, NULL, '2026-09-03 13:43:07', '2026-09-03 13:58:45', 991, 1);
INSERT INTO `exam_appointment` VALUES (16, 21, 4, '2026-09-04', '08:00:00', 299.00, 2, 'qweqweqweqweqweqweqweq', '2026-09-03 13:57:02', '2026-09-03 13:57:59', 502, 1);
INSERT INTO `exam_appointment` VALUES (17, 4, 4, '2026-09-04', '08:00:00', 299.00, 2, '', '2026-09-03 20:36:56', '2026-09-03 20:38:51', 991, 1);
INSERT INTO `exam_appointment` VALUES (18, 4, 1, '2026-09-04', '08:00:00', 199.00, 2, '', '2026-09-03 20:42:45', '2026-09-03 20:44:25', 502, 1);

-- ----------------------------
-- Table structure for exam_appointment_item
-- ----------------------------
DROP TABLE IF EXISTS `exam_appointment_item`;
CREATE TABLE `exam_appointment_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '体检记录明细ID',
  `appointment_id` bigint NOT NULL COMMENT '体检记录ID',
  `exam_item_id` bigint NOT NULL COMMENT '体检项目ID',
  `item_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称快照',
  `result_value` decimal(10, 2) NULL DEFAULT NULL COMMENT '数值型结果',
  `result_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '结果单位',
  `result_text` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文本型结果',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0待检查 1正常 2异常 3未完成',
  `abnormal` tinyint NOT NULL DEFAULT 0 COMMENT '是否异常：0正常 1异常',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '体检记录明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_appointment_item
-- ----------------------------
INSERT INTO `exam_appointment_item` VALUES (12, 3, 5, '空腹血糖', 6.10, 'mmol/L', NULL, 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (13, 3, 6, '血脂', 6.20, 'mmol/L', NULL, 2, 1, '总胆固醇偏高', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (14, 3, 7, '心电图', NULL, NULL, '窦性心律，未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (15, 3, 10, '胸部CT', NULL, NULL, '双肺未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (16, 5, 1, '血常规', NULL, NULL, '红细胞、白细胞、血小板均正常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (17, 5, 2, '尿常规', NULL, NULL, '未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (18, 5, 3, '肝功能', 25.00, 'U/L', NULL, 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (19, 5, 4, '肾功能', 78.00, 'μmol/L', NULL, 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (20, 5, 5, '空腹血糖', 6.80, 'mmol/L', NULL, 2, 1, '空腹血糖偏高', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (21, 5, 6, '血脂', 5.90, 'mmol/L', NULL, 2, 1, '总胆固醇偏高', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (22, 5, 7, '心电图', NULL, NULL, '窦性心律，未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (23, 5, 8, '腹部彩超', NULL, NULL, '肝胆胰脾未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (24, 5, 9, '骨密度检测', -2.10, 'T值', NULL, 2, 1, '骨量减少', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (25, 5, 10, '胸部CT', NULL, NULL, '双肺未见明显异常', 1, 0, '正常', '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_appointment_item` VALUES (26, 3, 5, '空腹血糖', 6.10, 'mmol/L', NULL, 1, 0, '正常', '2026-08-30 16:16:03', '2026-08-30 16:16:03');
INSERT INTO `exam_appointment_item` VALUES (27, 3, 6, '血脂', 6.20, 'mmol/L', NULL, 2, 1, '总胆固醇偏高', '2026-08-30 16:16:03', '2026-08-30 16:16:03');
INSERT INTO `exam_appointment_item` VALUES (28, 3, 7, '心电图', NULL, NULL, '窦性心律，未见明显异常', 1, 0, '正常', '2026-08-30 16:16:03', '2026-08-30 16:16:03');
INSERT INTO `exam_appointment_item` VALUES (29, 3, 10, '胸部CT', NULL, NULL, '双肺未见明显异常', 1, 0, '正常', '2026-08-30 16:16:03', '2026-08-30 16:16:03');
INSERT INTO `exam_appointment_item` VALUES (32, 8, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-02 00:03:10', '2026-09-02 00:03:10');
INSERT INTO `exam_appointment_item` VALUES (33, 8, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-02 00:03:10', '2026-09-02 00:03:10');
INSERT INTO `exam_appointment_item` VALUES (34, 8, 9, '骨密度检测', NULL, NULL, NULL, 0, 0, NULL, '2026-09-02 00:03:10', '2026-09-02 00:03:10');
INSERT INTO `exam_appointment_item` VALUES (35, 9, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:16:48', '2026-09-03 09:16:48');
INSERT INTO `exam_appointment_item` VALUES (36, 9, 2, '尿常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:16:48', '2026-09-03 09:16:48');
INSERT INTO `exam_appointment_item` VALUES (37, 9, 3, '肝功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:16:48', '2026-09-03 09:16:48');
INSERT INTO `exam_appointment_item` VALUES (38, 9, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:16:48', '2026-09-03 09:16:48');
INSERT INTO `exam_appointment_item` VALUES (39, 9, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:16:48', '2026-09-03 09:16:48');
INSERT INTO `exam_appointment_item` VALUES (40, 10, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (41, 10, 2, '尿常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (42, 10, 3, '肝功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (43, 10, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (44, 10, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (45, 10, 6, '血脂', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (46, 10, 7, '心电图', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (47, 10, 8, '腹部彩超', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (48, 10, 9, '骨密度检测', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (49, 10, 10, '胸部CT', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:22', '2026-09-03 09:39:22');
INSERT INTO `exam_appointment_item` VALUES (50, 11, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:48', '2026-09-03 09:39:48');
INSERT INTO `exam_appointment_item` VALUES (51, 11, 6, '血脂', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:48', '2026-09-03 09:39:48');
INSERT INTO `exam_appointment_item` VALUES (52, 11, 7, '心电图', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:48', '2026-09-03 09:39:48');
INSERT INTO `exam_appointment_item` VALUES (53, 11, 10, '胸部CT', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 09:39:48', '2026-09-03 09:39:48');
INSERT INTO `exam_appointment_item` VALUES (54, 12, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:44');
INSERT INTO `exam_appointment_item` VALUES (55, 12, 2, '尿常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:44');
INSERT INTO `exam_appointment_item` VALUES (56, 12, 3, '肝功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:44');
INSERT INTO `exam_appointment_item` VALUES (57, 12, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:44');
INSERT INTO `exam_appointment_item` VALUES (58, 12, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 10:43:44', '2026-09-03 10:43:44');
INSERT INTO `exam_appointment_item` VALUES (59, 13, 1, '血常规', NULL, NULL, NULL, 1, 0, NULL, '2026-09-03 11:02:02', '2026-09-03 14:15:54');
INSERT INTO `exam_appointment_item` VALUES (60, 14, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (61, 14, 2, '尿常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (62, 14, 3, '肝功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (63, 14, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (64, 14, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (65, 14, 6, '血脂', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (66, 14, 7, '心电图', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (67, 14, 8, '腹部彩超', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (68, 14, 9, '骨密度检测', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (69, 14, 10, '胸部CT', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 11:44:12', '2026-09-03 11:44:12');
INSERT INTO `exam_appointment_item` VALUES (70, 15, 1, '血常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 13:43:07', '2026-09-03 13:43:07');
INSERT INTO `exam_appointment_item` VALUES (71, 15, 2, '尿常规', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 13:43:07', '2026-09-03 13:43:07');
INSERT INTO `exam_appointment_item` VALUES (72, 15, 3, '肝功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 13:43:07', '2026-09-03 13:43:07');
INSERT INTO `exam_appointment_item` VALUES (73, 15, 4, '肾功能', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 13:43:07', '2026-09-03 13:43:07');
INSERT INTO `exam_appointment_item` VALUES (74, 15, 5, '空腹血糖', NULL, NULL, NULL, 0, 0, NULL, '2026-09-03 13:43:07', '2026-09-03 13:43:07');
INSERT INTO `exam_appointment_item` VALUES (75, 16, 1, '血常规', 222.00, 'ww', 'weq', 1, 1, 'qwe', '2026-09-03 13:57:02', '2026-09-03 13:57:59');
INSERT INTO `exam_appointment_item` VALUES (76, 16, 4, '肾功能', 222.00, 'ww', 'qwe', 1, 0, 'qwe', '2026-09-03 13:57:02', '2026-09-03 13:57:59');
INSERT INTO `exam_appointment_item` VALUES (77, 16, 9, '骨密度检测', 222.00, 'ww', 'qwe', 1, 0, 'qwe', '2026-09-03 13:57:02', '2026-09-03 13:57:59');
INSERT INTO `exam_appointment_item` VALUES (78, 17, 1, '血常规', NULL, NULL, '健康', 1, 0, NULL, '2026-09-03 20:36:56', '2026-09-03 20:38:51');
INSERT INTO `exam_appointment_item` VALUES (79, 17, 4, '肾功能', NULL, NULL, '健康', 1, 0, NULL, '2026-09-03 20:36:56', '2026-09-03 20:38:51');
INSERT INTO `exam_appointment_item` VALUES (80, 17, 9, '骨密度检测', NULL, NULL, '疏松', 1, 1, '少剧烈运动', '2026-09-03 20:36:56', '2026-09-03 20:38:51');
INSERT INTO `exam_appointment_item` VALUES (81, 18, 1, '血常规', NULL, NULL, '正常', 1, 0, NULL, '2026-09-03 20:42:45', '2026-09-03 20:44:25');
INSERT INTO `exam_appointment_item` VALUES (82, 18, 2, '尿常规', NULL, NULL, '正常', 1, 0, NULL, '2026-09-03 20:42:45', '2026-09-03 20:44:25');
INSERT INTO `exam_appointment_item` VALUES (83, 18, 3, '肝功能', 45.00, 'U/L', '偏高', 1, 1, NULL, '2026-09-03 20:42:45', '2026-09-03 20:44:25');
INSERT INTO `exam_appointment_item` VALUES (84, 18, 4, '肾功能', 125.00, 'umol/L', '偏高', 1, 1, NULL, '2026-09-03 20:42:45', '2026-09-03 20:44:25');
INSERT INTO `exam_appointment_item` VALUES (85, 18, 5, '空腹血糖', 6.70, 'mmol/L', '偏高', 1, 1, NULL, '2026-09-03 20:42:45', '2026-09-03 20:44:25');

-- ----------------------------
-- Table structure for exam_item
-- ----------------------------
DROP TABLE IF EXISTS `exam_item`;
CREATE TABLE `exam_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '体检项目ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名称',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '单项价格',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `result_type` tinyint NOT NULL DEFAULT 0 COMMENT '结果类型：0文本 1数值',
  `reference_min` decimal(10, 2) NULL DEFAULT NULL COMMENT '参考范围下限',
  `reference_max` decimal(10, 2) NULL DEFAULT NULL COMMENT '参考范围上限',
  `reference_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参考范围单位',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目说明',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0禁用 1启用',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '体检项目表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_item
-- ----------------------------
INSERT INTO `exam_item` VALUES (1, '血常规', 35.00, '次', 0, NULL, NULL, NULL, '检测红细胞、白细胞、血小板等指标', 1, 1, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (2, '尿常规', 25.00, '次', 0, NULL, NULL, NULL, '检查尿液相关指标', 1, 2, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (3, '肝功能', 80.00, '次', 1, 0.00, 40.00, 'U/L', '检测谷丙转氨酶等肝功能指标', 1, 3, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (4, '肾功能', 70.00, '次', 1, 40.00, 100.00, 'μmol/L', '检测肌酐等肾功能指标', 1, 4, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (5, '空腹血糖', 20.00, '次', 1, 3.90, 6.10, 'mmol/L', '检测空腹血糖水平', 1, 5, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (6, '血脂', 60.00, '次', 1, 0.00, 5.20, 'mmol/L', '检测总胆固醇等血脂指标', 1, 6, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (7, '心电图', 50.00, '次', 0, NULL, NULL, NULL, '检查心脏电生理活动情况', 1, 7, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (8, '腹部彩超', 120.00, '次', 0, NULL, NULL, NULL, '检查肝脏、胆囊、胰腺、脾脏等', 1, 8, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (9, '骨密度检测', 100.00, '次', 1, -1.00, 10.00, 'T值', '检测骨骼密度情况', 1, 9, '2026-08-30 16:17:15', '2026-08-30 16:17:15');
INSERT INTO `exam_item` VALUES (10, '胸部CT', 200.00, '次', 0, NULL, NULL, NULL, '检查肺部及胸部相关情况', 1, 10, '2026-08-30 16:17:15', '2026-08-30 16:17:15');

-- ----------------------------
-- Table structure for exam_package
-- ----------------------------
DROP TABLE IF EXISTS `exam_package`;
CREATE TABLE `exam_package`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '体检套餐ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '套餐价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '套餐图片',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '套餐说明',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0下架 1上架',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '体检套餐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_package
-- ----------------------------
INSERT INTO `exam_package` VALUES (1, '基础体检套餐', 199.00, NULL, '适合身体状况较好的老年人进行基础健康检查', 1, 1, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package` VALUES (2, '老年健康套餐', 399.00, NULL, '针对老年人常见健康问题设计的综合体检套餐', 1, 2, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package` VALUES (3, '心脑血管专项套餐', 499.00, NULL, '针对心脑血管健康进行专项检查', 1, 3, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package` VALUES (4, '骨健康套餐', 299.00, NULL, '针对老年人骨骼健康进行专项检查', 1, 4, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package` VALUES (5, '全面体检套餐', 699.00, NULL, '包含多个身体系统的综合健康检查', 1, 5, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package` VALUES (11, '11', 3.00, NULL, '12', 1, 7, '2026-09-01 22:47:10', '2026-09-01 22:47:10');

-- ----------------------------
-- Table structure for exam_package_item
-- ----------------------------
DROP TABLE IF EXISTS `exam_package_item`;
CREATE TABLE `exam_package_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `package_id` bigint NOT NULL COMMENT '体检套餐ID',
  `exam_item_id` bigint NOT NULL COMMENT '体检项目ID',
  `sort` int NOT NULL DEFAULT 0 COMMENT '项目排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '体检套餐项目关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of exam_package_item
-- ----------------------------
INSERT INTO `exam_package_item` VALUES (5, 2, 1, 1, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (6, 2, 2, 2, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (7, 2, 3, 3, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (8, 2, 4, 4, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (9, 2, 5, 5, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (10, 2, 6, 6, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (11, 2, 7, 7, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (12, 3, 5, 1, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (13, 3, 6, 2, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (14, 3, 7, 3, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (15, 3, 10, 4, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (16, 4, 1, 1, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (17, 4, 4, 2, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (18, 4, 9, 3, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (19, 5, 1, 1, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (20, 5, 2, 2, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (21, 5, 3, 3, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (22, 5, 4, 4, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (23, 5, 5, 5, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (24, 5, 6, 6, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (25, 5, 7, 7, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (26, 5, 8, 8, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (27, 5, 9, 9, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (28, 5, 10, 10, NULL, '2026-08-30 16:14:48', '2026-08-30 16:14:48');
INSERT INTO `exam_package_item` VALUES (49, 11, 1, 0, NULL, '2026-09-01 22:47:36', '2026-09-01 22:47:36');
INSERT INTO `exam_package_item` VALUES (50, 1, 1, 1, NULL, '2026-09-03 09:13:17', '2026-09-03 09:13:17');
INSERT INTO `exam_package_item` VALUES (51, 1, 2, 2, NULL, '2026-09-03 09:13:17', '2026-09-03 09:13:17');
INSERT INTO `exam_package_item` VALUES (52, 1, 3, 3, NULL, '2026-09-03 09:13:17', '2026-09-03 09:13:17');
INSERT INTO `exam_package_item` VALUES (53, 1, 4, 4, NULL, '2026-09-03 09:13:17', '2026-09-03 09:13:17');
INSERT INTO `exam_package_item` VALUES (54, 1, 5, 5, NULL, '2026-09-03 09:13:17', '2026-09-03 09:13:17');

-- ----------------------------
-- Table structure for family_member
-- ----------------------------
DROP TABLE IF EXISTS `family_member`;
CREATE TABLE `family_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `elder_id` bigint NOT NULL COMMENT '关联老人ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '家属姓名',
  `relation` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '与老人关系',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `id_card_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '身份证号',
  `is_primary` tinyint NOT NULL DEFAULT 0 COMMENT '是否主要联系人：0否，1是',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '家庭住址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除：0否，1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_family_member_elder_id`(`elder_id` ASC) USING BTREE,
  INDEX `idx_family_member_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '老人家属表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of family_member
-- ----------------------------
INSERT INTO `family_member` VALUES (1, 22, '张丽', '女儿', '13900001001', '110101197601011234', 1, '北京市朝阳区幸福路18号', '日常主要联系人', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `family_member` VALUES (2, 22, '张伟', '儿子', '13900001002', '110101197801021235', 0, '北京市朝阳区幸福路18号', '紧急联系人', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `family_member` VALUES (3, 23, '王敏', '女儿', '13900001003', '110101197903082346', 1, '北京市海淀区康乐街26号', '日常主要联系人', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');
INSERT INTO `family_member` VALUES (4, 23, '刘强', '女婿', '13900001004', '110101197803082347', 0, '北京市海淀区康乐街26号', '紧急联系人', 0, '2026-09-02 19:59:52', '2026-09-02 19:59:52');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint NOT NULL COMMENT '所属上级',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `type` tinyint NOT NULL DEFAULT 0 COMMENT '类型(0:目录,1:菜单,2:按钮)',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `permission_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限值',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态(1:正常，0:禁止)',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0（true）未删除， 1（false）已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 0, '权限管理', 0, NULL, NULL, 'ArrowDownBold', 0, NULL, 0, '2026-08-28 11:30:57', '2026-09-02 20:57:43');
INSERT INTO `permission` VALUES (2, 1, '用户管理', 1, '/user', NULL, 'Avatar', NULL, NULL, 0, '2026-08-28 11:31:29', '2026-08-31 15:34:52');
INSERT INTO `permission` VALUES (3, 1, '角色管理', 1, '/role', NULL, 'Briefcase', NULL, NULL, 0, '2026-08-28 11:31:53', '2026-08-31 15:34:45');
INSERT INTO `permission` VALUES (4, 1, '权限管理', 1, '/permission', NULL, 'InfoFilled', NULL, NULL, 0, '2026-08-28 11:32:27', '2026-08-31 15:34:34');
INSERT INTO `permission` VALUES (5, 2, '用户添加', 2, NULL, 'user:add', NULL, NULL, NULL, 0, '2026-08-28 11:33:04', '2026-08-28 11:33:16');
INSERT INTO `permission` VALUES (6, 2, '用户删除', 2, NULL, 'user:deleteById', NULL, NULL, NULL, 0, '2026-08-28 11:34:12', '2026-08-28 11:34:44');
INSERT INTO `permission` VALUES (7, 3, '角色添加', 2, NULL, 'role:add', NULL, NULL, NULL, 0, '2026-08-28 11:34:41', '2026-08-28 11:34:41');
INSERT INTO `permission` VALUES (8, 0, '老人管理', 1, '/elder', NULL, 'UserFilled', 1, NULL, 0, '2026-08-28 11:35:09', '2026-09-01 09:13:47');
INSERT INTO `permission` VALUES (9, 0, '标签管理', 1, '/tag', NULL, 'CollectionTag', 6, NULL, 0, '2026-08-28 11:35:30', '2026-09-02 20:57:31');
INSERT INTO `permission` VALUES (15, 0, '护理管理', 0, NULL, NULL, 'Plus', 4, NULL, 0, '2026-08-31 16:45:41', '2026-09-02 20:57:23');
INSERT INTO `permission` VALUES (16, 15, '护理项目', 1, '/care-item', NULL, 'DataAnalysis', 1, NULL, 0, '2026-08-31 16:48:54', '2026-08-31 16:49:38');
INSERT INTO `permission` VALUES (17, 16, '护理等级', 2, NULL, NULL, NULL, 2, NULL, 1, '2026-08-31 16:51:29', '2026-08-31 16:51:34');
INSERT INTO `permission` VALUES (18, 15, '护理等级', 1, '/care-level', NULL, 'ScaleToOriginal', 2, NULL, 0, '2026-08-31 16:52:09', '2026-09-01 14:01:18');
INSERT INTO `permission` VALUES (19, 15, '护理计划', 1, '/care-plan', NULL, 'Calendar', 3, NULL, 0, '2026-08-31 16:52:29', '2026-09-01 14:01:27');
INSERT INTO `permission` VALUES (20, 15, '护理任务', 1, '/care-task', NULL, 'Checked', 4, NULL, 0, '2026-08-31 16:52:44', '2026-09-01 14:01:40');
INSERT INTO `permission` VALUES (21, 9, '标签添加', 2, NULL, 'tag:add', NULL, 2, NULL, 0, '2026-09-02 09:19:24', '2026-09-02 09:19:24');
INSERT INTO `permission` VALUES (22, 9, '标签删除', 2, NULL, 'tag:deleteById', NULL, 2, NULL, 0, '2026-09-02 09:19:58', '2026-09-02 09:19:58');
INSERT INTO `permission` VALUES (23, 19, '添加计划', 2, NULL, 'carePlan:add', NULL, 1, NULL, 0, '2026-09-02 09:20:53', '2026-09-02 09:20:53');
INSERT INTO `permission` VALUES (24, 19, '删除计划', 2, NULL, 'carePlan:deleteAll', NULL, 2, NULL, 0, '2026-09-02 09:21:20', '2026-09-02 09:21:20');
INSERT INTO `permission` VALUES (25, 20, '删除任务', 2, NULL, 'careTask:deleteAll', NULL, 1, NULL, 0, '2026-09-02 09:22:13', '2026-09-02 09:22:13');
INSERT INTO `permission` VALUES (26, 16, '删除项目', 2, NULL, 'careItem:deleteAll', NULL, 1, NULL, 0, '2026-09-02 10:14:38', '2026-09-02 10:14:38');
INSERT INTO `permission` VALUES (27, 16, '添加项目', 2, NULL, 'careItem:add', NULL, 2, NULL, 0, '2026-09-02 13:29:56', '2026-09-02 13:30:34');
INSERT INTO `permission` VALUES (28, 18, '添加等级', 2, NULL, 'careLevel:add', NULL, 1, NULL, 0, '2026-09-02 13:35:53', '2026-09-02 13:35:53');
INSERT INTO `permission` VALUES (29, 18, '删除等级', 2, NULL, 'careLevel:deleteAll', NULL, 2, NULL, 0, '2026-09-02 13:36:15', '2026-09-02 13:36:15');
INSERT INTO `permission` VALUES (30, 0, '体检管理', 0, '', NULL, 'Crop', 5, NULL, 0, '2026-09-02 17:01:17', '2026-09-02 20:57:27');
INSERT INTO `permission` VALUES (31, 30, '体检项目', 1, '/exam-item', NULL, 'Apple', 3, NULL, 0, '2026-09-02 17:02:47', '2026-09-03 13:59:53');
INSERT INTO `permission` VALUES (32, 30, '体检套餐', 1, '/exam-package', NULL, 'Box', 4, NULL, 0, '2026-09-02 17:03:21', '2026-09-03 13:59:58');
INSERT INTO `permission` VALUES (33, 0, '家属管理', 1, '/family-member', 'familyMember:list', 'UserFilled', 2, 1, 0, '2026-09-02 19:56:05', '2026-09-02 20:56:59');
INSERT INTO `permission` VALUES (34, 33, '新增家属', 2, NULL, 'familyMember:add', NULL, 1, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (35, 33, '编辑家属', 2, NULL, 'familyMember:update', NULL, 2, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (36, 33, '删除家属', 2, NULL, 'familyMember:delete', NULL, 3, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (37, 0, '床位管理', 1, '/bed', 'bed:list', 'House', 3, 1, 0, '2026-09-02 19:56:05', '2026-09-02 20:57:04');
INSERT INTO `permission` VALUES (38, 37, '新增床位', 2, NULL, 'bed:add', NULL, 1, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (39, 37, '编辑床位', 2, NULL, 'bed:update', NULL, 2, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (40, 37, '办理入住/退床', 2, NULL, 'bed:assign', NULL, 3, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (41, 37, '删除床位', 2, NULL, 'bed:delete', NULL, 4, 1, 0, '2026-09-02 19:56:05', '2026-09-02 19:56:05');
INSERT INTO `permission` VALUES (42, 30, '体检预约管理', 1, '/exam-appointment', NULL, 'Calendar', 1, 1, 0, '2026-09-03 10:08:46', '2026-09-03 10:08:46');
INSERT INTO `permission` VALUES (43, 30, '体检执行', 1, '/exam-execution', NULL, 'Finished', 2, 1, 0, '2026-09-03 11:18:56', '2026-09-03 13:59:47');
INSERT INTO `permission` VALUES (44, 43, '修改体检报告', 2, NULL, 'examExecution:updateReport', NULL, 1, 1, 0, '2026-09-03 14:15:04', '2026-09-03 14:15:04');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色名称',
  `code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色编码',
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0（true）未删除， 1（false）已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '管理员', 'admin', NULL, 0, '2026-08-28 10:30:39', '2026-08-28 10:30:39');
INSERT INTO `role` VALUES (2, '护工', 'hugong', NULL, 0, '2026-08-28 16:08:51', '2026-09-02 09:46:38');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0（true）未删除， 1（false）已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 270 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (213, 1, 1, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (214, 1, 2, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (215, 1, 5, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (216, 1, 6, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (217, 1, 3, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (218, 1, 7, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (219, 1, 4, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (220, 1, 8, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (221, 1, 33, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (222, 1, 34, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (223, 1, 35, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (224, 1, 36, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (225, 1, 37, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (226, 1, 38, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (227, 1, 39, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (228, 1, 40, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (229, 1, 41, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (230, 1, 15, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (231, 1, 16, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (232, 1, 26, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (233, 1, 27, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (234, 1, 18, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (235, 1, 28, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (236, 1, 29, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (237, 1, 19, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (238, 1, 23, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (239, 1, 24, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (240, 1, 20, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (241, 1, 25, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (242, 1, 30, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (243, 1, 42, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (244, 1, 43, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (245, 1, 44, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (246, 1, 31, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (247, 1, 32, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (248, 1, 9, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (249, 1, 21, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (250, 1, 22, 0, '2026-09-03 14:15:27', '2026-09-03 14:15:27');
INSERT INTO `role_permission` VALUES (251, 2, 8, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (252, 2, 33, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (253, 2, 34, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (254, 2, 35, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (255, 2, 36, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (256, 2, 37, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (257, 2, 38, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (258, 2, 39, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (259, 2, 40, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (260, 2, 41, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (261, 2, 15, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (262, 2, 19, 0, '2026-09-03 17:03:01', '2026-09-03 17:03:01');
INSERT INTO `role_permission` VALUES (263, 2, 23, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (264, 2, 20, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (265, 2, 25, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (266, 2, 30, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (267, 2, 42, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (268, 2, 43, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');
INSERT INTO `role_permission` VALUES (269, 2, 44, 0, '2026-09-03 17:03:02', '2026-09-03 17:03:02');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签编码（LIVE_ALONE/EMPTY_NEST/...）',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称（独居/空巢/...）',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tag_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, 'LIVE_ALONE', '独居', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (2, 'EMPTY_NEST', '空巢', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (3, 'AGE_80', '高龄', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (4, 'DISABLED', '失能', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (5, 'C_HBP', '高血压', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (6, 'C_DM', '糖尿病', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (7, 'C_CHD', '冠心病', 0, '2026-08-26 16:25:05', '2026-08-26 16:25:05');
INSERT INTO `tag` VALUES (8, 'tst1', 'tst1', 0, '2026-08-26 16:55:59', '2026-08-26 16:55:59');
INSERT INTO `tag` VALUES (9, 'tst2', 'tst2', 0, '2026-08-26 17:20:50', '2026-08-26 17:20:50');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码哈希',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0：停用，1：正常）',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除（0：未删除，1：已删除）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1021 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '123', '123', '123', '123', '0', 1, 0, '2026-08-25 17:04:27', '2026-08-26 16:35:24');
INSERT INTO `user` VALUES (502, 'hugong5', '123', '177-2618-1233', 'caoxiuying413@outlook.com', '0', 1, 0, '2026-07-16 09:08:37', '2026-09-03 09:10:31');
INSERT INTO `user` VALUES (613, 'hugong1', '123', '20-940-4900', 'zheng5@outlook.com', '0', 1, 0, '2026-08-19 16:49:54', '2026-09-02 09:45:53');
INSERT INTO `user` VALUES (749, 'hugong3', '123', '136-6654-5106', 'shihanzhou@gmail.com', '0', 1, 0, '2026-08-10 00:21:35', '2026-09-03 09:10:04');
INSERT INTO `user` VALUES (860, 'hugong4', '123', '769-568-0141', 'ldong41@mail.com', '0', 1, 0, '2026-08-02 15:35:11', '2026-09-03 09:10:16');
INSERT INTO `user` VALUES (991, 'hugong2', '123', '153-9258-6505', 'lei56@gmail.com', '0', 1, 0, '2026-08-15 23:04:06', '2026-09-02 09:46:11');


-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0（true）未删除， 1（false）已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工-角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (5, 1, 1, 0, '2026-09-02 09:47:45', '2026-09-02 09:47:45');
INSERT INTO `user_role` VALUES (6, 2, 613, 0, '2026-09-02 09:47:48', '2026-09-02 09:47:48');
INSERT INTO `user_role` VALUES (7, 2, 991, 0, '2026-09-02 09:47:50', '2026-09-02 09:47:50');
INSERT INTO `user_role` VALUES (8, 2, 749, 0, '2026-09-03 09:10:08', '2026-09-03 09:10:08');
INSERT INTO `user_role` VALUES (9, 2, 860, 0, '2026-09-03 09:10:20', '2026-09-03 09:10:20');
INSERT INTO `user_role` VALUES (10, 2, 502, 0, '2026-09-03 09:10:35', '2026-09-03 09:10:35');

SET FOREIGN_KEY_CHECKS = 1;