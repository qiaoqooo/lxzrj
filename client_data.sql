-- ============================================================
-- 客户交付版假数据 SQL
-- 数据库：boss_employment
-- 客户账号：id=4  role可切换(recruiter/seeker)
-- ============================================================
-- 功能覆盖：
--   1. 求职者模式：投递简历到其他公司的岗位
--   2. 招聘者模式：查看收到的求职者投递的简历
-- ============================================================

-- =====================
-- 0. 清理旧的测试数据（可重复执行）
-- =====================
DELETE FROM `job_applications` WHERE `seeker_id` = 4;
DELETE FROM `job_applications` WHERE `recruiter_id` = 4;
DELETE FROM `site_messages` WHERE `sender_id` = 4 OR `receiver_id` = 4;
DELETE FROM `resume_attachments` WHERE `user_id` = 4;
DELETE FROM `resumes` WHERE `user_id` = 4;
DELETE FROM `user_job_preferences` WHERE `user_id` = 4;
DELETE FROM `jobs` WHERE `recruiter_id` = 4;
DELETE FROM `companies` WHERE `owner_user_id` = 4;

-- 清理假的求职者和招聘者（openid以fake_开头的）
DELETE FROM `job_applications` WHERE `seeker_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `job_applications` WHERE `recruiter_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `site_messages` WHERE `sender_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `site_messages` WHERE `receiver_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `resume_attachments` WHERE `user_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `resumes` WHERE `user_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `user_job_preferences` WHERE `user_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `jobs` WHERE `recruiter_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `companies` WHERE `owner_user_id` IN (SELECT id FROM `users` WHERE `openid` LIKE 'fake_%');
DELETE FROM `users` WHERE `openid` LIKE 'fake_%';

-- =====================
-- 1. 补全客户账号信息
-- =====================
UPDATE `users` SET
  `real_name` = '赵经理',
  `nickname` = '赵经理',
  `gender` = 1,
  `city` = '上海'
WHERE `id` = 4;

-- ============================================================
-- ======= 第一部分：招聘者模式 - 查看收到的简历 =======
-- ============================================================

-- =====================
-- 2. 创建客户的公司（归属 id=4）
-- =====================
INSERT INTO `companies` (`name`, `logo_url`, `city`, `district`, `address`, `financing_stage`, `staff_size`, `industry`, `homepage_url`, `description`, `owner_user_id`, `created_at`, `updated_at`)
VALUES
('智远信息科技有限公司',
 'https://ui-avatars.com/api/?name=智远&background=4ecdc4&color=fff&size=100&font-size=0.4',
 '上海', '浦东新区', '上海市浦东新区张江高科技园区',
 'A轮', '50-99人', '互联网/移动互联网',
 NULL,
 '智远信息科技有限公司专注于企业级SaaS产品研发，为中小企业提供数字化转型解决方案。团队成员均来自一线互联网公司，拥有丰富的产品和技术经验。',
 4, NOW(), NOW());

SET @my_company_id = LAST_INSERT_ID();

-- =====================
-- 3. 创建客户公司的在招职位（5个，status=open）
-- =====================
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@my_company_id, 4, 'Java后端开发工程师', 15000, 28000, '上海', '浦东新区', '张江高科技园区', '2-4年', '本科', '全职',
'岗位职责：\n1. 负责公司SaaS平台后端开发\n2. 参与系统架构设计和技术选型\n3. 编写高质量代码，参与代码评审\n\n任职要求：\n1. 本科及以上，计算机相关专业\n2. 2年以上Java开发经验\n3. 熟悉Spring Boot、MySQL、Redis',
'open', NOW(), NOW());
SET @my_job1 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@my_company_id, 4, '前端开发工程师', 13000, 22000, '上海', '浦东新区', '张江高科技园区', '1-3年', '本科', '全职',
'岗位职责：\n1. 负责Web端和小程序端开发\n2. 优化前端性能和用户体验\n3. 维护前端组件库\n\n任职要求：\n1. 本科及以上学历\n2. 1年以上前端经验\n3. 熟悉Vue.js或React，了解小程序开发',
'open', NOW(), NOW());
SET @my_job2 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@my_company_id, 4, '产品经理', 18000, 30000, '上海', '浦东新区', '张江高科技园区', '3-5年', '本科', '全职',
'岗位职责：\n1. 负责SaaS产品规划和需求管理\n2. 输出PRD，跟进开发上线\n3. 基于数据驱动产品迭代\n\n任职要求：\n1. 本科及以上学历\n2. 3年以上B端产品经验\n3. 优秀的逻辑分析和沟通能力',
'open', NOW(), NOW());
SET @my_job3 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@my_company_id, 4, '测试工程师', 10000, 18000, '上海', '浦东新区', '张江高科技园区', '1-3年', '大专', '全职',
'岗位职责：\n1. 负责产品功能和接口测试\n2. 编写测试用例和测试报告\n3. 跟踪Bug修复情况\n\n任职要求：\n1. 大专及以上学历\n2. 1年以上测试经验\n3. 熟悉常用测试工具',
'open', NOW(), NOW());
SET @my_job4 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@my_company_id, 4, 'UI设计师', 12000, 20000, '上海', '浦东新区', '张江高科技园区', '1-3年', '本科', '全职',
'岗位职责：\n1. 负责产品视觉和交互设计\n2. 制定设计规范\n3. 配合前端保证还原度\n\n任职要求：\n1. 本科及以上，设计相关专业\n2. 1年以上UI经验\n3. 精通Figma、Sketch',
'open', NOW(), NOW());
SET @my_job5 = LAST_INSERT_ID();

-- =====================
-- 4. 创建3个假求职者（用于投递到客户公司）
-- =====================
INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_seeker_001', '13900000001', '张明', '/images/user_avatar.jpg', '张明', 'seeker', 1, '上海', NOW(), NOW());
SET @seeker1 = LAST_INSERT_ID();

INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_seeker_002', '13900000002', '李静', '/images/user_avatar.jpg', '李静', 'seeker', 2, '北京', NOW(), NOW());
SET @seeker2 = LAST_INSERT_ID();

INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_seeker_003', '13900000003', '陈浩', '/images/user_avatar.jpg', '陈浩', 'seeker', 1, '深圳', NOW(), NOW());
SET @seeker3 = LAST_INSERT_ID();

-- =====================
-- 5. 创建假求职者的简历 + 附件
-- =====================

-- 求职者1：张明
INSERT INTO `resumes` (`user_id`, `title`, `name`, `gender`, `city`, `work_experience`, `is_default`, `created_at`, `updated_at`)
VALUES
(@seeker1, 'Java开发工程师', '张明', 1, '上海',
'2022.06 - 至今  上海云智科技  Java开发工程师\n- 负责后端API开发，使用Spring Boot框架\n- 参与数据库设计和SQL优化\n\n2020.07 - 2022.05  杭州数联软件  初级开发\n- 参与ERP系统功能模块开发\n- 编写单元测试',
1, NOW(), NOW());
SET @resume_s1 = LAST_INSERT_ID();

INSERT INTO `resume_attachments` (`user_id`, `resume_id`, `file_name`, `file_url`, `file_size`, `preview_image_url`, `created_at`)
VALUES (@seeker1, @resume_s1, '张明_Java开发工程师.pdf', 'E:\\test_pdf\\test_pdf.pdf', 1024000, NULL, NOW());
SET @attach_s1 = LAST_INSERT_ID();

-- 求职者2：李静
INSERT INTO `resumes` (`user_id`, `title`, `name`, `gender`, `city`, `work_experience`, `is_default`, `created_at`, `updated_at`)
VALUES
(@seeker2, '前端开发工程师', '李静', 2, '北京',
'2023.03 - 至今  北京字节互动  前端开发\n- 负责小程序和H5页面开发\n- 使用Vue3 + TypeScript技术栈\n\n2021.07 - 2023.02  广州微创网络  前端实习\n- 参与电商页面开发\n- 负责移动端适配',
1, NOW(), NOW());
SET @resume_s2 = LAST_INSERT_ID();

INSERT INTO `resume_attachments` (`user_id`, `resume_id`, `file_name`, `file_url`, `file_size`, `preview_image_url`, `created_at`)
VALUES (@seeker2, @resume_s2, '李静_前端开发工程师.pdf', 'E:\\test_pdf\\test_pdf.pdf', 1024000, NULL, NOW());
SET @attach_s2 = LAST_INSERT_ID();

-- 求职者3：陈浩
INSERT INTO `resumes` (`user_id`, `title`, `name`, `gender`, `city`, `work_experience`, `is_default`, `created_at`, `updated_at`)
VALUES
(@seeker3, '产品经理', '陈浩', 1, '深圳',
'2021.04 - 至今  深圳快链科技  高级产品经理\n- 负责B端SaaS产品从0到1\n- 管理3人产品团队\n\n2019.07 - 2021.03  成都云端信息  产品经理\n- 负责CRM产品需求分析\n- 推动产品迭代上线20+版本',
1, NOW(), NOW());
SET @resume_s3 = LAST_INSERT_ID();

INSERT INTO `resume_attachments` (`user_id`, `resume_id`, `file_name`, `file_url`, `file_size`, `preview_image_url`, `created_at`)
VALUES (@seeker3, @resume_s3, '陈浩_产品经理.pdf', 'E:\\test_pdf\\test_pdf.pdf', 1024000, NULL, NOW());
SET @attach_s3 = LAST_INSERT_ID();

-- =====================
-- 6. 创建投递记录（假求职者 → 客户的公司职位）
--    招聘者模式下可查看这些投递
-- =====================

-- 张明 → Java后端 (applied)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job1, @seeker1, 4, @resume_s1, @attach_s1, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR));

-- 张明 → 测试工程师 (communicating)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job4, @seeker1, 4, @resume_s1, @attach_s1, 'communicating', '求职者对岗位很感兴趣', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 李静 → 前端开发 (applied)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job2, @seeker2, 4, @resume_s2, @attach_s2, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- 李静 → UI设计师 (interview_pending)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job5, @seeker2, 4, @resume_s2, @attach_s2, 'interview_pending', '已约本周三下午面试', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 陈浩 → 产品经理 (applied)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job3, @seeker3, 4, @resume_s3, @attach_s3, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 5 HOUR), DATE_SUB(NOW(), INTERVAL 5 HOUR));

-- 陈浩 → Java后端 (communicating)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@my_job1, @seeker3, 4, @resume_s3, @attach_s3, 'communicating', '求职者有丰富的项目管理经验', DATE_SUB(NOW(), INTERVAL 4 DAY), DATE_SUB(NOW(), INTERVAL 4 DAY));

-- ============================================================
-- ======= 第二部分：求职者模式 - 投递简历到其他公司 =======
-- ============================================================

-- =====================
-- 7. 创建3个假招聘者用户
-- =====================
INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_recruiter_001', '13800000001', '王HR', '/images/user_avatar.jpg', '王芳', 'recruiter', 2, '北京', NOW(), NOW());
SET @recruiter1 = LAST_INSERT_ID();

INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_recruiter_002', '13800000002', '刘经理', '/images/user_avatar.jpg', '刘伟', 'recruiter', 1, '深圳', NOW(), NOW());
SET @recruiter2 = LAST_INSERT_ID();

INSERT INTO `users` (`openid`, `phone`, `nickname`, `avatar_url`, `real_name`, `role`, `gender`, `city`, `created_at`, `updated_at`)
VALUES
('fake_recruiter_003', '13800000003', '孙主管', '/images/user_avatar.jpg', '孙丽', 'recruiter', 2, '广州', NOW(), NOW());
SET @recruiter3 = LAST_INSERT_ID();

-- =====================
-- 8. 创建3个假公司
-- =====================
INSERT INTO `companies` (`name`, `logo_url`, `city`, `district`, `address`, `financing_stage`, `staff_size`, `industry`, `homepage_url`, `description`, `owner_user_id`, `created_at`, `updated_at`)
VALUES
('北京飞云科技有限公司',
 'https://ui-avatars.com/api/?name=飞云&background=3498db&color=fff&size=100&font-size=0.4',
 '北京', '朝阳区', '北京市朝阳区望京SOHO',
 'C轮', '500-999人', '人工智能',
 NULL, '飞云科技是一家专注于AI技术落地的创新型企业，核心产品覆盖智能客服、智能推荐等领域，服务超过500家企业客户。',
 @recruiter1, NOW(), NOW());
SET @comp1 = LAST_INSERT_ID();

INSERT INTO `companies` (`name`, `logo_url`, `city`, `district`, `address`, `financing_stage`, `staff_size`, `industry`, `homepage_url`, `description`, `owner_user_id`, `created_at`, `updated_at`)
VALUES
('深圳星河数据科技有限公司',
 'https://ui-avatars.com/api/?name=星河&background=e74c3c&color=fff&size=100&font-size=0.4',
 '深圳', '南山区', '深圳市南山区科技园',
 'B轮', '100-499人', '大数据',
 NULL, '星河数据致力于大数据分析和商业智能服务，帮助企业实现数据驱动决策。已获得知名VC数千万融资。',
 @recruiter2, NOW(), NOW());
SET @comp2 = LAST_INSERT_ID();

INSERT INTO `companies` (`name`, `logo_url`, `city`, `district`, `address`, `financing_stage`, `staff_size`, `industry`, `homepage_url`, `description`, `owner_user_id`, `created_at`, `updated_at`)
VALUES
('广州云端互联网络有限公司',
 'https://ui-avatars.com/api/?name=云端&background=2ecc71&color=fff&size=100&font-size=0.4',
 '广州', '天河区', '广州市天河区珠江新城',
 'A轮', '50-99人', '电子商务',
 NULL, '云端互联是一家新锐电商技术服务商，为品牌商提供全渠道电商解决方案，团队年轻有活力。',
 @recruiter3, NOW(), NOW());
SET @comp3 = LAST_INSERT_ID();

-- =====================
-- 9. 创建其他公司的在招职位（每家2个，共6个）
-- =====================

-- 飞云科技
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp1, @recruiter1, 'AI算法工程师', 25000, 45000, '北京', '朝阳区', '望京SOHO', '3-5年', '硕士', '全职',
'岗位职责：\n1. 负责NLP/CV算法研发和优化\n2. 推动算法模型工程化落地\n3. 跟踪前沿技术，保持技术领先\n\n任职要求：\n1. 硕士及以上，AI相关方向\n2. 3年以上算法经验\n3. 熟悉PyTorch/TensorFlow',
'open', NOW(), NOW());
SET @other_job1 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp1, @recruiter1, 'Go后端开发工程师', 20000, 35000, '北京', '朝阳区', '望京SOHO', '2-4年', '本科', '全职',
'岗位职责：\n1. 负责高并发后端服务开发\n2. 参与微服务架构设计\n3. 保障系统稳定性和性能\n\n任职要求：\n1. 本科及以上学历\n2. 2年以上Go开发经验\n3. 熟悉分布式系统设计',
'open', NOW(), NOW());
SET @other_job2 = LAST_INSERT_ID();

-- 星河数据
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp2, @recruiter2, '数据分析师', 15000, 25000, '深圳', '南山区', '科技园', '1-3年', '本科', '全职',
'岗位职责：\n1. 负责业务数据分析和报告\n2. 搭建数据看板和监控体系\n3. 挖掘数据价值驱动业务增长\n\n任职要求：\n1. 本科及以上学历\n2. 1年以上数据分析经验\n3. 熟练使用SQL和Python',
'open', NOW(), NOW());
SET @other_job3 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp2, @recruiter2, 'Java开发工程师', 16000, 28000, '深圳', '南山区', '科技园', '2-4年', '本科', '全职',
'岗位职责：\n1. 负责大数据平台后端开发\n2. 优化数据处理性能\n3. 参与系统架构升级\n\n任职要求：\n1. 本科及以上学历\n2. 2年以上Java经验\n3. 了解Hadoop/Spark/Flink优先',
'open', NOW(), NOW());
SET @other_job4 = LAST_INSERT_ID();

-- 云端互联
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp3, @recruiter3, '电商运营经理', 12000, 20000, '广州', '天河区', '珠江新城', '2-4年', '本科', '全职',
'岗位职责：\n1. 负责电商平台店铺日常运营\n2. 制定营销推广策略\n3. 分析运营数据优化转化率\n\n任职要求：\n1. 本科及以上学历\n2. 2年以上电商运营经验\n3. 有成功的运营案例',
'open', NOW(), NOW());
SET @other_job5 = LAST_INSERT_ID();

INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@comp3, @recruiter3, '全栈开发工程师', 18000, 30000, '广州', '天河区', '珠江新城', '2-4年', '本科', '全职',
'岗位职责：\n1. 负责电商系统全栈开发\n2. 前后端功能开发和联调\n3. 保障系统性能和安全\n\n任职要求：\n1. 本科及以上学历\n2. 2年以上全栈开发经验\n3. 熟悉Vue/React + Java/Node.js',
'open', NOW(), NOW());
SET @other_job6 = LAST_INSERT_ID();

-- =====================
-- 10. 创建客户（id=4）的简历和附件（求职者模式使用）
-- =====================
INSERT INTO `resumes` (`user_id`, `title`, `name`, `gender`, `city`, `work_experience`, `is_default`, `created_at`, `updated_at`)
VALUES
(4, '全栈开发工程师', '赵经理', 1, '上海',
'2022.06 - 至今  上海智远信息科技  技术负责人\n- 负责公司SaaS产品技术架构设计\n- 管理5人开发团队\n- 主导完成系统从0到1搭建\n\n2019.07 - 2022.05  杭州网易  高级Java开发\n- 参与电商中台系统开发\n- 负责订单和支付核心模块\n- 系统日均处理50万+订单',
1, NOW(), NOW());
SET @my_resume = LAST_INSERT_ID();

INSERT INTO `resume_attachments` (`user_id`, `resume_id`, `file_name`, `file_url`, `file_size`, `preview_image_url`, `created_at`)
VALUES (4, @my_resume, '赵经理_全栈开发工程师.pdf', 'E:\\test_pdf\\test_pdf.pdf', 1024000, NULL, NOW());
SET @my_attach = LAST_INSERT_ID();

-- 求职意向
INSERT INTO `user_job_preferences` (`user_id`, `status_text`, `expected_city`, `expected_industry`, `expected_job`, `expected_salary`, `is_public`, `created_at`, `updated_at`)
VALUES (4, '在职-考虑机会', '上海,北京,深圳', '互联网/移动互联网', '技术负责人/全栈开发', '25k-40k', 1, NOW(), NOW());

-- =====================
-- 11. 创建客户投递到其他公司的记录（求职者模式可查看）
-- =====================

-- 投递到飞云科技 - AI算法工程师 (applied)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@other_job1, 4, @recruiter1, @my_resume, @my_attach, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR));

-- 投递到飞云科技 - Go后端 (communicating)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@other_job2, 4, @recruiter1, @my_resume, @my_attach, 'communicating', 'HR已查看简历', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 投递到星河数据 - Java开发 (interview_pending)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@other_job4, 4, @recruiter2, @my_resume, @my_attach, 'interview_pending', '已约下周一面试', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 投递到云端互联 - 全栈开发 (applied)
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES (@other_job6, 4, @recruiter3, @my_resume, @my_attach, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 6 HOUR), DATE_SUB(NOW(), INTERVAL 6 HOUR));

-- =====================
-- 12. 创建聊天消息（丰富数据）
-- =====================

-- 张明 和 客户的对话
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES (@seeker1, 4, '您好，我对贵公司的Java后端开发工程师职位很感兴趣，请问还在招吗？', 0, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR));
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES (4, @seeker1, '你好，目前还在招，你方便发一下简历吗？', 0, DATE_SUB(NOW(), INTERVAL 50 MINUTE), DATE_SUB(NOW(), INTERVAL 50 MINUTE));

-- 李静 和 客户的对话
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES (@seeker2, 4, '赵经理您好，看到贵公司前端岗位，我有2年Vue开发经验，希望能聊聊。', 0, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- 客户 和 飞云科技的对话（求职者视角）
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES (4, @recruiter1, '您好，我对贵公司的Go后端开发岗位很感兴趣，方便聊聊吗？', 0, DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES (@recruiter1, 4, '你好！欢迎投递，看了你的简历背景不错，方便约个电话聊一下吗？', 0, DATE_SUB(NOW(), INTERVAL 23 HOUR), DATE_SUB(NOW(), INTERVAL 23 HOUR));

-- ============================================================
-- 验证查询（可选执行）
-- ============================================================

-- 招聘者模式：查看客户公司的职位
-- SELECT id, title, salary_min, salary_max, status FROM jobs WHERE recruiter_id = 4;

-- 招聘者模式：查看收到的投递（应有6条）
-- SELECT ja.id, ja.job_id, ja.seeker_id, ja.status, u.nickname as seeker_name
-- FROM job_applications ja LEFT JOIN users u ON ja.seeker_id = u.id
-- WHERE ja.recruiter_id = 4;

-- 求职者模式：查看其他公司的职位（可投递）
-- SELECT j.id, j.title, c.name as company_name, j.salary_min, j.salary_max
-- FROM jobs j LEFT JOIN companies c ON j.company_id = c.id
-- WHERE j.recruiter_id != 4 AND j.status = 'open';

-- 求职者模式：查看自己的投递记录（应有4条）
-- SELECT ja.id, ja.job_id, ja.recruiter_id, ja.status
-- FROM job_applications ja WHERE ja.seeker_id = 4;

-- ============================================================
-- 数据总览：
-- ============================================================
-- 客户公司：智远信息科技有限公司（5个在招职位）
-- 其他公司：飞云科技 / 星河数据 / 云端互联（各2个职位，共6个）
-- 假求职者：张明 / 李静 / 陈浩（共投递6次到客户公司）
-- 客户投递：投递4次到其他公司
-- 聊天消息：5条
-- 所有简历附件地址：E:\test_pdf\test_pdf.pdf
-- ============================================================

