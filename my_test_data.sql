-- ============================================================
-- 测试假数据 SQL（基于真实用户数据）
-- 数据库：boss_employment
-- 招聘者：id=3  openid=oVHTu5CTh7KnH2nwuRKNmdtow8O (recruiter)
-- 求职者：id=2  openid=oVHTu5DqNkKcxAYse-6k9xqdYAl8 (seeker)
-- ============================================================

-- =====================
-- 0. 清理之前的测试数据（防止重复执行报错）
-- =====================
DELETE FROM `job_applications` WHERE `seeker_id` = 2 AND `recruiter_id` = 3;
DELETE FROM `resume_attachments` WHERE `user_id` = 2;
DELETE FROM `resumes` WHERE `user_id` = 2;
DELETE FROM `jobs` WHERE `recruiter_id` = 3;
DELETE FROM `companies` WHERE `owner_user_id` = 3;

-- =====================
-- 1. 补全求职者（id=2）的用户信息
-- =====================
UPDATE `users` SET
  `real_name` = '王小明',
  `gender` = 1,
  `city` = '深圳',
  `nickname` = '王小明'
WHERE `id` = 2;

-- 补全招聘者（id=3）的用户信息
UPDATE `users` SET
  `real_name` = '李经理',
  `gender` = 1,
  `city` = '北京',
  `nickname` = '李经理'
WHERE `id` = 3;

-- =====================
-- 2. 创建公司（归属招聘者 id=3）
-- =====================
INSERT INTO `companies` (`name`, `logo_url`, `city`, `district`, `address`, `financing_stage`, `staff_size`, `industry`, `homepage_url`, `description`, `owner_user_id`, `created_at`, `updated_at`)
VALUES
('星辰互联科技有限公司',
 'https://ui-avatars.com/api/?name=星辰&background=4ecdc4&color=fff&size=100&font-size=0.4',
 '北京', '海淀区', '北京市海淀区中关村科技园区8号楼',
 'B轮', '100-499人', '互联网/移动互联网',
 'https://www.example.com',
 '星辰互联科技有限公司成立于2020年，专注于移动互联网产品研发，致力于为用户提供优质的求职招聘服务。公司团队由来自BAT的资深工程师组成，技术实力雄厚。',
 3, NOW(), NOW());

-- 获取公司ID
SET @company_id = LAST_INSERT_ID();

-- =====================
-- 3. 创建职位（归属招聘者 id=3，状态 open）
-- =====================

-- 职位1：Java高级开发工程师
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@company_id, 3, 'Java高级开发工程师', 18000, 30000, '北京', '海淀区', '中关村科技园区8号楼', '3-5年', '本科', '全职',
'岗位职责：
1. 负责公司核心业务系统的架构设计与开发
2. 参与微服务架构设计，保障系统高可用
3. 主导技术方案评审，输出技术文档
4. 指导初中级开发人员，提升团队技术水平

任职要求：
1. 本科及以上学历，计算机相关专业
2. 3年以上Java后端开发经验
3. 精通Spring Boot、Spring Cloud、MyBatis-Plus
4. 熟悉MySQL调优、Redis缓存、消息队列
5. 有良好的编码习惯和团队协作能力',
'open', NOW(), NOW());

SET @job1_id = LAST_INSERT_ID();

-- 职位2：前端开发工程师
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@company_id, 3, '前端开发工程师', 15000, 25000, '北京', '海淀区', '中关村科技园区8号楼', '1-3年', '本科', '全职',
'岗位职责：
1. 负责公司Web端和小程序端产品的前端开发
2. 与产品、UI紧密协作，实现高质量的交互效果
3. 持续优化前端性能和用户体验
4. 参与前端基础设施建设和组件库维护

任职要求：
1. 本科及以上学历，计算机或相关专业
2. 1年以上前端开发经验
3. 精通Vue.js或React，熟悉微信小程序开发
4. 掌握HTML5、CSS3、ES6+、TypeScript
5. 有移动端适配经验者优先',
'open', NOW(), NOW());

SET @job2_id = LAST_INSERT_ID();

-- 职位3：产品经理
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@company_id, 3, '产品经理', 20000, 35000, '北京', '海淀区', '中关村科技园区8号楼', '3-5年', '本科', '全职',
'岗位职责：
1. 负责招聘类产品的需求分析和功能规划
2. 输出PRD文档，跟进开发进度和上线质量
3. 基于数据分析驱动产品迭代优化
4. 深入了解用户需求，持续提升产品竞争力

任职要求：
1. 本科及以上学历
2. 3年以上互联网产品经验，有招聘/HR方向优先
3. 熟练使用Axure、Figma等原型工具
4. 优秀的逻辑分析能力和跨部门沟通能力
5. 有0到1产品经验者优先',
'open', NOW(), NOW());

SET @job3_id = LAST_INSERT_ID();

-- 职位4：测试工程师
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@company_id, 3, '测试工程师', 12000, 20000, '北京', '海淀区', '中关村科技园区8号楼', '1-3年', '大专', '全职',
'岗位职责：
1. 负责产品的功能测试、接口测试和性能测试
2. 编写测试计划和测试用例，输出测试报告
3. 跟踪Bug修复，保障产品质量
4. 参与自动化测试框架搭建

任职要求：
1. 大专及以上学历，计算机相关专业
2. 1年以上软件测试经验
3. 熟悉常用测试工具（Postman、JMeter等）
4. 了解自动化测试（Selenium/Appium优先）
5. 细心负责，具备良好的文档能力',
'open', NOW(), NOW());

SET @job4_id = LAST_INSERT_ID();

-- 职位5：UI设计师
INSERT INTO `jobs` (`company_id`, `recruiter_id`, `title`, `salary_min`, `salary_max`, `city`, `district`, `work_place`, `experience_req`, `degree_req`, `job_type`, `description`, `status`, `created_at`, `updated_at`)
VALUES
(@company_id, 3, 'UI设计师', 13000, 22000, '北京', '海淀区', '中关村科技园区8号楼', '1-3年', '本科', '全职',
'岗位职责：
1. 负责公司产品的界面视觉设计
2. 制定和维护设计规范，保障设计一致性
3. 与前端开发紧密配合，确保设计还原度
4. 参与用户研究，持续优化视觉体验

任职要求：
1. 本科及以上学历，设计相关专业
2. 1年以上UI设计经验，有作品集
3. 精通Figma、Sketch、Photoshop等设计工具
4. 对移动端设计规范有深入理解
5. 有B端或招聘类产品设计经验优先',
'open', NOW(), NOW());

SET @job5_id = LAST_INSERT_ID();

-- =====================
-- 4. 创建求职者（id=2）的在线简历
-- =====================
INSERT INTO `resumes` (`user_id`, `title`, `name`, `gender`, `city`, `work_experience`, `is_default`, `created_at`, `updated_at`)
VALUES
(2, 'Java全栈开发工程师', '王小明', 1, '深圳',
'1. 2023.03 - 至今  深圳市云端科技有限公司  Java开发工程师
   - 负责电商平台后端核心模块开发，使用Spring Boot + MyBatis-Plus
   - 独立完成订单系统重构，QPS提升40%
   - 参与微服务拆分，负责用户中心和支付模块

2. 2021.07 - 2023.02  广州极客软件有限公司  初级Java开发
   - 参与企业OA系统后端开发
   - 负责报表模块和权限管理模块
   - 编写单元测试，保障代码质量',
1, NOW(), NOW());

SET @resume_id = LAST_INSERT_ID();

-- =====================
-- 5. 创建求职者（id=2）的附件简历
-- =====================
INSERT INTO `resume_attachments` (`user_id`, `resume_id`, `file_name`, `file_url`, `file_size`, `preview_image_url`, `created_at`)
VALUES
(2, @resume_id, '王小明_Java全栈开发工程师.pdf', 'E:\\test_pdf\\test_pdf.pdf', 1024000, NULL, NOW());

SET @attachment_id = LAST_INSERT_ID();

-- =====================
-- 6. 创建求职者（id=2）的求职意向
-- =====================
INSERT INTO `user_job_preferences` (`user_id`, `status_text`, `expected_city`, `expected_industry`, `expected_job`, `expected_salary`, `is_public`, `created_at`, `updated_at`)
VALUES
(2, '在职-考虑机会', '北京,深圳', '互联网/移动互联网', 'Java开发工程师', '18k-30k', 1, NOW(), NOW());

-- =====================
-- 7. 创建投递记录（求职者 id=2 → 招聘者 id=3 的职位）
--    这样招聘者登录后可以在"收到的简历"中看到这些投递
-- =====================

-- 投递职位1：Java高级开发工程师 - 状态: applied（已投递）
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES
(@job1_id, 2, 3, @resume_id, @attachment_id, 'applied', NULL, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR));

-- 投递职位2：前端开发工程师 - 状态: communicating（沟通中）
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES
(@job2_id, 2, 3, @resume_id, @attachment_id, 'communicating', '求职者希望了解团队技术栈', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 投递职位3：产品经理 - 状态: interview_pending（待面试）
INSERT INTO `job_applications` (`job_id`, `seeker_id`, `recruiter_id`, `resume_id`, `resume_attachment_id`, `status`, `remark`, `created_at`, `updated_at`)
VALUES
(@job3_id, 2, 3, @resume_id, @attachment_id, 'interview_pending', '已约面试，时间待定', DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY));

-- =====================
-- 8. 创建一条沟通消息（让招聘者和求职者有聊天记录）
-- =====================
INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES
(2, 3, '您好，我对贵公司的Java高级开发工程师职位很感兴趣，方便聊聊吗？', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 2 HOUR));

INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES
(3, 2, '你好！欢迎投递，你之前做过哪些项目？', 0, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR));

INSERT INTO `site_messages` (`sender_id`, `receiver_id`, `content`, `is_read`, `created_at`, `updated_at`)
VALUES
(2, 3, '我之前在云端科技负责电商平台后端开发，主要用Spring Boot + MyBatis-Plus，也有微服务拆分经验。', 0, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 30 MINUTE));

-- ============================================================
-- 验证查询（可选执行）
-- ============================================================

-- 查看公司
-- SELECT * FROM companies WHERE owner_user_id = 3;

-- 查看职位（应该5条，状态都是open）
-- SELECT id, title, salary_min, salary_max, status FROM jobs WHERE recruiter_id = 3;

-- 查看简历
-- SELECT * FROM resumes WHERE user_id = 2;

-- 查看投递记录（招聘者视角：收到的简历）
-- SELECT * FROM job_applications WHERE recruiter_id = 3;

-- 查看投递记录（求职者视角：我的投递）
-- SELECT * FROM job_applications WHERE seeker_id = 2;

-- 查看聊天记录
-- SELECT * FROM site_messages WHERE (sender_id = 2 AND receiver_id = 3) OR (sender_id = 3 AND receiver_id = 2) ORDER BY created_at;

-- ============================================================
-- 功能对照说明：
-- ============================================================
-- 【招聘者账号 id=3 登录后可以看到】
--   ✅ 公司信息（星辰互联科技有限公司）
--   ✅ 5个在招职位（状态open）
--   ✅ 3条收到的简历投递（applied / communicating / interview_pending）
--   ✅ 与求职者的聊天记录
--
-- 【求职者账号 id=2 登录后可以看到】
--   ✅ 自己的在线简历和附件简历
--   ✅ 3条投递记录
--   ✅ 5个可投递的职位（属于不同用户发布，不会触发"不能投递自己的职位"）
--   ✅ 与招聘者的聊天记录
-- ============================================================



