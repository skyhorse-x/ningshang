-- 安徽宁商科技集团 数据库初始化脚本
CREATE DATABASE IF NOT EXISTS ningshang DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ningshang;

-- 新闻表
CREATE TABLE IF NOT EXISTS news (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    summary VARCHAR(500),
    body TEXT,
    category VARCHAR(50),
    category_name VARCHAR(50),
    date VARCHAR(50),
    author VARCHAR(100),
    source VARCHAR(100),
    image VARCHAR(200),
    news_id VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 团队成员表
CREATE TABLE IF NOT EXISTS team_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    position VARCHAR(100),
    description VARCHAR(500),
    avatar VARCHAR(200),
    gradient VARCHAR(50),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 企业荣誉表
CREATE TABLE IF NOT EXISTS honor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(50),
    image VARCHAR(200),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 招聘岗位表
CREATE TABLE IF NOT EXISTS job (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    headcount VARCHAR(50),
    location VARCHAR(50),
    education VARCHAR(50),
    description VARCHAR(500),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 留言表
CREATE TABLE IF NOT EXISTS message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    type VARCHAR(50),
    content TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 子公司表
CREATE TABLE IF NOT EXISTS subsidiary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    english_name VARCHAR(100),
    category VARCHAR(50),
    description VARCHAR(500),
    logo VARCHAR(200),
    background VARCHAR(200),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 里程碑表
CREATE TABLE IF NOT EXISTS milestone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year VARCHAR(20) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始数据

-- 新闻数据
INSERT INTO news (title, summary, body, category, category_name, date, author, source, image, news_id) VALUES
('安徽宁商科技集团正式成立 扎根合肥赋能区域科创', '安徽宁商科技集团有限公司正式成立，多产业全链条布局完成。', '<p>安徽宁商科技集团有限公司正式成立，多产业全链条布局完成，统筹工程建设、数字软件、法务信息、智能装备四大核心板块协同发展。</p>', 'group', '集团新闻', '2026-07', '宁商科技集团品牌策划部', '内部资料', 'images/news-establishment.jpeg', 'group-nshang-established'),
('安徽宁商科技集团官方网站正式上线', '近日，安徽宁商科技集团官方网站正式开通运行。', '<p>近日，安徽宁商科技集团官方网站正式开通运行。作为集团对外品牌展示与权威信息发布的核心窗口，官网将全方位呈现企业发展面貌。</p>', 'group', '集团新闻', '2026-08', '宁商科技集团品牌策划部', '内部资料', 'images/news-website-online.jpeg', 'group-website-online'),
('安徽宁商集团2026下半年度工作部署会议顺利召开', '会议总结强调，月度绩效复盘是提质增效、查漏补缺的重要抓手。', '<p>会议总结强调，月度绩效复盘是提质增效、查漏补缺的重要抓手。各部门要强化跨部门协同联动，聚焦品牌体系建设、高新资质申报、组织架构优化、财务规范管理四大核心任务。</p>', 'group', '集团新闻', '2026-07', '宁商科技集团品牌策划部', '内部资料', 'images/news-work-deployment.png', 'group-work-deployment');

-- 团队成员数据
INSERT INTO team_member (name, position, description, avatar, gradient, sort_order) VALUES
('王 力', '创始人 · 董事长 · 总工程师', '澳大利亚昆士兰大学哲学博士（Ph.D.），集团战略总设计师。', 'images/team-wang-li.jpeg', 'linear-gradient(135deg,#1a365d 0%,#2c5282 100%)', 1),
('顾婷婷', '副总裁', '安徽大学法学学士，2026年6月加入集团。', 'images/team-gu-tingting.jpeg', 'linear-gradient(135deg,#5a2d82 0%,#7c3aed 100%)', 2),
('胡骁彤', '法定代表人 · 董事长助理', '安徽师范大学学前教育专业本科毕业。', 'images/team-hu-xiaotong.jpeg', 'linear-gradient(135deg,#7c2d12 0%,#c2410c 100%)', 3);

-- 荣誉数据
INSERT INTO honor (title, description, icon, image, sort_order) VALUES
('国家高新技术企业', '申报中，持续攻坚核心自研技术。', '🏅', 'images/honor-1.png', 1),
('安徽省软件行业协会会员单位', '深度参与软件行业生态建设。', '💻', 'images/honor-2.png', 2),
('合肥市建设工程优秀企业', '以匠心品质铸就精品工程。', '🏗', 'images/honor-3.png', 3),
('ISO9001质量管理体系认证', '建立健全质量管理体系。', '✓', 'images/honor-4.png', 4),
('十余项自主软件知识产权', '累计取得十余项自主软件知识产权。', '©', 'images/honor-5.png', 5),
('多项工程总承包专业资质', '持有工程总承包多项专业资质。', '📜', 'images/honor-6.png', 6);

-- 里程碑数据
INSERT INTO milestone (year, title, description, sort_order) VALUES
('2026.07.28', '宁商集团党支部启动筹备成立', '构建党建引领企业发展体系。', 1),
('2026.07.27', '胡骁彤正式接任法定代表人', '推动公司治理规范化发展。', 2),
('2026.07.24', '顾婷婷正式出任集团副总裁', '全面负责各业务板块运营。', 3),
('2026.07.22', '安徽宁商科技集团正式成立', '多产业全链条布局完成。', 4),
('2026.05.07', '安徽利至高建设工程有限公司成立', '开启集团产业布局新篇章。', 5);

-- 子公司数据
INSERT INTO subsidiary (name, english_name, category, description, logo, background, sort_order) VALUES
('安徽利至高建设工程有限公司', 'CONSTRUCTION ENGINEERING', '建设工程', '深耕工程建设与基础设施配套服务。', 'images/sub-logo-4.png', 'images/sub-bg-1.png', 1),
('安徽陆洲科技有限责任公司', 'TECHNOLOGY', '智能科技', '聚焦智能化配套服务与数字技术应用。', 'images/sub-logo-1.png', 'images/sub-bg-2.png', 2),
('安徽合州信息咨询有限责任公司', 'INFORMATION CONSULTING', '信息咨询', '提供专业信息咨询与企业科创服务。', 'images/sub-logo-2.png', 'images/sub-bg-3.png', 3),
('安徽省玉彤智能装备有限公司', 'INTELLIGENT EQUIPMENT', '智能装备', '聚焦人工智能、智能制造等前沿领域。', 'images/sub-logo-3.png', 'images/sub-bg-4.png', 4),
('合肥南峰建设投资有限公司', 'CONSTRUCTION INVESTMENT', '建设投资', '专注建设投资与城市服务领域。', 'images/sub-logo-5.png', 'images/sub-bg-5.png', 5);
