-- 在已有 exam_appointment 表执行一次
ALTER TABLE exam_appointment
  ADD COLUMN IF NOT EXISTS caregiver_id BIGINT NULL COMMENT '负责护工ID',
  ADD COLUMN IF NOT EXISTS assignment_status TINYINT NOT NULL DEFAULT 0 COMMENT '0未分配 1已分配';
CREATE INDEX idx_exam_appointment_caregiver ON exam_appointment (caregiver_id);
CREATE INDEX idx_exam_appointment_assignment ON exam_appointment (assignment_status);

/* 从这里开始可单独执行：初始化体检管理权限 */
INSERT INTO permission (parent_id, name, type, icon, sort, status, deleted, create_time, update_time)
SELECT 0, '体检管理', 0, 'DataAnalysis', 80, 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0);

SET @exam_root_id := (SELECT id FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0 ORDER BY id LIMIT 1);

INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检预约管理', 1, '/exam-appointment', 'Calendar', 1, 1, 0, NOW(), NOW()
WHERE @exam_root_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-appointment' AND deleted = 0);

INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检执行管理', 1, '/exam-execution', 'Finished', 2, 1, 0, NOW(), NOW()
WHERE @exam_root_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-execution' AND deleted = 0);

INSERT INTO role_permission (role_id, permission_id, create_time, update_time)
SELECT r.id, p.id, NOW(), NOW()
FROM role r JOIN permission p ON p.path IN ('/exam-appointment', '/exam-execution') AND p.deleted = 0
WHERE r.code IN ('admin', 'administrator', 'hugong')
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

-- 体检报告修改按钮权限：仅创建，不自动授予角色；请在角色管理中按需分配
SET @exam_execution_id := (SELECT id FROM permission WHERE path = '/exam-execution' AND deleted = 0 LIMIT 1);
INSERT INTO permission (parent_id, name, type, permission_value, sort, status, deleted, create_time, update_time)
SELECT @exam_execution_id, '修改体检报告', 2, 'examExecution:updateReport', 1, 1, 0, NOW(), NOW()
WHERE @exam_execution_id IS NOT NULL
  AND NOT EXISTS (SELECT 1 FROM permission WHERE permission_value = 'examExecution:updateReport' AND deleted = 0);
-- 将体检预约加入权限树，并授予管理员角色（重复执行不会重复插入）
INSERT INTO permission (parent_id, name, type, icon, sort, status, deleted, create_time, update_time)
SELECT 0, '体检管理', 0, 'DataAnalysis', 80, 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0);
SET @exam_root_id = (SELECT id FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0 LIMIT 1);
INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检预约管理', 1, '/exam-appointment', 'Calendar', 1, 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-appointment' AND deleted = 0);
INSERT INTO role_permission (role_id, permission_id, create_time, update_time)
SELECT r.id, p.id, NOW(), NOW() FROM role r JOIN permission p ON p.path = '/exam-appointment' AND p.deleted = 0
WHERE r.code IN ('admin', 'administrator') AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);

INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检执行管理', 1, '/exam-execution', 'Finished', 2, 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-execution' AND deleted = 0);
INSERT INTO role_permission (role_id, permission_id, create_time, update_time)
SELECT r.id, p.id, NOW(), NOW() FROM role r JOIN permission p ON p.path = '/exam-execution' AND p.deleted = 0
WHERE r.code IN ('admin', 'administrator', 'hugong') AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);


--写在下面

/* 从这里开始可单独执行：初始化体检管理权限 */
INSERT INTO permission (parent_id, name, type, icon, sort, status, deleted, create_time, update_time)
SELECT 0, '体检管理', 0, 'DataAnalysis', 80, 1, 0, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0);

SET @exam_root_id := (SELECT id FROM permission WHERE parent_id = 0 AND name = '体检管理' AND deleted = 0 ORDER BY id LIMIT 1);

INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检预约管理', 1, '/exam-appointment', 'Calendar', 1, 1, 0, NOW(), NOW()
WHERE @exam_root_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-appointment' AND deleted = 0);

INSERT INTO permission (parent_id, name, type, path, icon, sort, status, deleted, create_time, update_time)
SELECT @exam_root_id, '体检执行管理', 1, '/exam-execution', 'Finished', 2, 1, 0, NOW(), NOW()
WHERE @exam_root_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM permission WHERE path = '/exam-execution' AND deleted = 0);

INSERT INTO role_permission (role_id, permission_id, create_time, update_time)
SELECT r.id, p.id, NOW(), NOW()
FROM role r JOIN permission p ON p.path IN ('/exam-appointment', '/exam-execution') AND p.deleted = 0
WHERE r.code IN ('admin', 'administrator', 'hugong')
  AND NOT EXISTS (SELECT 1 FROM role_permission rp WHERE rp.role_id = r.id AND rp.permission_id = p.id);
