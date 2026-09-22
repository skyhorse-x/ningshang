package com.ningshang.config;

import com.ningshang.entity.*;
import com.ningshang.repository.*;
import com.ningshang.service.AdminService;
import com.ningshang.service.CoreBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private NewsRepository newsRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private HonorRepository honorRepository;
    @Autowired private SubsidiaryRepository subsidiaryRepository;
    @Autowired private CoreBusinessRepository coreBusinessRepository;
    @Autowired private CoreBusinessService coreBusinessService;
    @Autowired private PartnerRepository partnerRepository;
    @Autowired private MilestoneRepository milestoneRepository;
    @Autowired private JobRepository jobRepository;
    @Autowired private SiteContentRepository siteContentRepository;
    @Autowired private AdminService adminService;
    @Autowired private AdminRepository adminRepository;
    @Autowired private AdminMenuRepository adminMenuRepository;
    @Autowired private AdminGroupRepository adminGroupRepository;
    @Autowired private AdminGroupMenuRepository adminGroupMenuRepository;
    @Autowired private AdminPermissionRepository adminPermissionRepository;
    @Autowired private AdminGroupPermissionRepository adminGroupPermissionRepository;
    @Autowired private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void run(String[] args) {
        // 1. 初始化后台菜单
        initMenus();

        // 2. 初始化权限组
        initGroups();

        // 3. 初始化组-菜单关联（超级管理员组拥有全部菜单）
        initGroupMenus();
        initPermissions();

        // 4. 创建默认管理员并关联到超级管理员组
        adminService.createDefaultAdmin();
        linkDefaultAdminToSuperGroup();

        // ddl-auto:update 不会可靠扩展已有文本列，启动时执行幂等迁移。
        String[] richTextColumns = {
                "news.body", "site_content.content", "job.description", "team_member.description",
                "honor.description", "milestone.description", "subsidiary.description", "core_business.description"
        };
        for (String column : richTextColumns) {
            String[] parts = column.split("\\.");
            try {
                jdbcTemplate.execute("ALTER TABLE `" + parts[0] + "` MODIFY COLUMN `" + parts[1] + "` LONGTEXT");
            } catch (Exception e) {
                // 首次启动时 Hibernate 已按 LONGTEXT 创建；不存在的可选表交给下次启动处理。
            }
        }

        if (newsRepository.count() == 0) initNews();
        if (teamMemberRepository.count() == 0) initTeamMembers();
        if (honorRepository.count() == 0) initHonors();
        if (subsidiaryRepository.count() == 0) initSubsidiaries();
        if (coreBusinessRepository.count() == 0) initCoreBusinesses();
        migrateSubsidiaryCoreBusinesses();
        correctLegacySubsidiaryCoreBusinesses();
        if (partnerRepository.count() == 0) initPartners();
        if (milestoneRepository.count() == 0) initMilestones();
        if (jobRepository.count() == 0) initJobs();
        initSiteContent();

        // 图片路径规范化：确保以 / 开头，避免前端子路由下相对路径失效
        normalizeImagePaths();
    }

    /** 为升级前的子公司按“子公司类型”补一次业务关联，后续完全以后台勾选结果为准。 */
    private void migrateSubsidiaryCoreBusinesses() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS app_migration (migration_key VARCHAR(100) PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        Integer applied = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_migration WHERE migration_key = ?", Integer.class,
                "subsidiary_core_business_v1");
        if (applied != null && applied > 0) return;
        java.util.List<CoreBusiness> businesses = coreBusinessRepository.findAllByOrderBySortOrderAscIdAsc();
        if (!businesses.isEmpty()) {
            for (Subsidiary subsidiary : subsidiaryRepository.findAll()) {
                if (subsidiary.getCoreBusinessIds() != null && !subsidiary.getCoreBusinessIds().isEmpty()) continue;
                CoreBusiness matched = coreBusinessService.matchByCategory(subsidiary.getCategory(), businesses);
                if (matched != null) {
                    subsidiary.setCoreBusinessIds(new java.util.ArrayList<>(java.util.Collections.singletonList(matched.getId())));
                    subsidiaryRepository.save(subsidiary);
                }
            }
        }
        jdbcTemplate.update("INSERT INTO app_migration (migration_key) VALUES (?)", "subsidiary_core_business_v1");
    }

    /** 旧数据恰好为五家公司、五项业务时，修正模糊匹配造成的重复关联。 */
    private void correctLegacySubsidiaryCoreBusinesses() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS app_migration (migration_key VARCHAR(100) PRIMARY KEY, applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        Integer applied = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM app_migration WHERE migration_key = ?", Integer.class,
                "subsidiary_core_business_v2");
        if (applied != null && applied > 0) return;
        java.util.List<Subsidiary> subsidiaries = subsidiaryRepository.findAllByOrderBySortOrderAscIdAsc();
        java.util.List<CoreBusiness> businesses = coreBusinessRepository.findAllByOrderBySortOrderAscIdAsc();
        if (subsidiaries.size() == businesses.size() && !subsidiaries.isEmpty()) {
            java.util.Set<Long> assigned = new java.util.HashSet<>();
            boolean hasMissingOrDuplicate = false;
            for (Subsidiary subsidiary : subsidiaries) {
                java.util.List<Long> ids = subsidiary.getCoreBusinessIds();
                if (ids == null || ids.size() != 1 || !assigned.add(ids.get(0))) {
                    hasMissingOrDuplicate = true;
                    break;
                }
            }
            if (hasMissingOrDuplicate) {
                for (int i = 0; i < subsidiaries.size(); i++) {
                    subsidiaries.get(i).setCoreBusinessIds(new java.util.ArrayList<>(
                            java.util.Collections.singletonList(businesses.get(i).getId())));
                    subsidiaryRepository.save(subsidiaries.get(i));
                }
            }
        }
        jdbcTemplate.update("INSERT INTO app_migration (migration_key) VALUES (?)", "subsidiary_core_business_v2");
    }

    private boolean isRelativeImage(String value) {
        return value != null && !value.isEmpty() && !value.startsWith("/")
            && !value.matches("^[a-zA-Z][a-zA-Z0-9+.-]*:.*");
    }

    private void normalizeImagePaths() {
        for (News n : newsRepository.findAll()) {
            if (isRelativeImage(n.getImage())) {
                n.setImage("/" + n.getImage());
                newsRepository.save(n);
            }
        }
        for (Subsidiary s : subsidiaryRepository.findAll()) {
            boolean changed = false;
            if (isRelativeImage(s.getLogo())) { s.setLogo("/" + s.getLogo()); changed = true; }
            if (isRelativeImage(s.getBackground())) { s.setBackground("/" + s.getBackground()); changed = true; }
            if (changed) subsidiaryRepository.save(s);
        }
        for (CoreBusiness b : coreBusinessRepository.findAll()) {
            if (isRelativeImage(b.getCoverImage())) {
                b.setCoverImage("/" + b.getCoverImage());
                coreBusinessRepository.save(b);
            }
        }
        for (TeamMember m : teamMemberRepository.findAll()) {
            if (isRelativeImage(m.getAvatar())) {
                m.setAvatar("/" + m.getAvatar());
                teamMemberRepository.save(m);
            }
        }
        for (Honor h : honorRepository.findAll()) {
            if (isRelativeImage(h.getImage())) {
                h.setImage("/" + h.getImage());
                honorRepository.save(h);
            }
        }
        for (Partner p : partnerRepository.findAll()) {
            if (isRelativeImage(p.getLogo())) {
                p.setLogo("/" + p.getLogo());
                partnerRepository.save(p);
            }
        }
    }

    private void initJobs() {
        String[][] data = {
            {"软件开发工程师", "技术部", "3人", "合肥", "本科及以上", "负责AI定制软件开发与系统集成项目"},
            {"建筑工程师", "工程部", "2人", "合肥", "本科及以上", "负责建筑工程施工管理与质量控制"},
            {"智能装备研发工程师", "研发部", "2人", "合肥", "硕士及以上", "负责智能装备产品研发与技术攻关"},
            {"法务专员", "法务部", "1人", "合肥", "本科及以上", "负责集团法律合规与风险管控"},
            {"市场拓展经理", "市场部", "2人", "合肥", "本科及以上", "负责市场调研与客户关系维护"},
            {"财务会计", "财务部", "1人", "合肥", "本科及以上", "负责日常财务核算与报表编制"}
        };
        for (int i = 0; i < data.length; i++) {
            Job job = new Job();
            job.setTitle(data[i][0]);
            job.setDepartment(data[i][1]);
            job.setHeadcount(data[i][2]);
            job.setLocation(data[i][3]);
            job.setEducation(data[i][4]);
            job.setDescription(data[i][5]);
            job.setSortOrder(i + 1);
            jobRepository.save(job);
        }
    }

    private void initSiteContent() {
        upsertContent("contact_address", "地址", "安徽省合肥市蜀山区24号创新工场", 1);
        upsertContent("contact_phone", "电话", "13365745652", 2);
        upsertContent("contact_email", "邮箱", "NStechnology@163.com", 3);
        upsertContent("site_logo", "网站主Logo", "/images/logo.png", 5);
        upsertContent("office_hours", "办公时间", "周一至周五　09:00 - 18:00", 4);
        upsertContent("about_intro_meta", "集团简介-副标题", "一徽藏一城，一潮见格局", 10);
        upsertContent("about_intro_s1_title", "集团简介-第一段标题", "徽韵承城，扎根本土根基", 11);
        upsertContent("about_intro_s1_body", "集团简介-第一段正文", "安徽宁商科技集团有限公司扎根安徽合肥，深度契合合肥\"城湖共生\"的城市格局与科创引领的发展脉络。", 12);
        upsertContent("about_intro_s2_title", "集团简介-第二段标题", "潮涌科创，锚定主业航向", 13);
        upsertContent("about_intro_s2_body", "集团简介-第二段正文", "集团以数字科技为核心发展主线，奔涌拓展业务边界，形成覆盖技术研发、系统集成、数字文创的多元核心业务体系。", 14);
        upsertContent("about_intro_s3_title", "集团简介-第三段标题", "五子联动，共筑产业潮头", 15);
        upsertContent("stat_founded", "统计-成立年份", "2026", 20);
        upsertContent("stat_companies", "统计-成员企业", "5", 21);
        upsertContent("stat_ip", "统计-知识产权", "10+", 22);
        upsertContent("stat_fields", "统计-业务领域", "5", 23);
        upsertContent("speech_chairman_name", "董事长致词-董事长姓名", "王 力", 28);
        upsertContent("speech_chairman_title", "董事长致词-董事长职位", "安徽宁商科技集团 董事长", 29);
        upsertContent("speech_quote", "董事长致词-引言", "徽商古训有云：\n\"诚为本，义为先，贾而好儒，行稳致远。\"", 30);
        upsertContent("speech_body", "董事长致词-正文",
                "尊敬的社会各界友人、合作伙伴，全体宁商同仁：\n\n千载江淮文脉，沉淀出实业兴邦的厚重底色；长三角一体化浪潮，奔涌着数字赋能的蓬勃生机。\n\n展望未来，集团将始终践行\"科创赋能城乡，数字服务实业\"的企业使命，以诚信立品牌之基，以创新拓产业之局。", 31);
        upsertContent("speech_sign", "董事长致词-落款", "安徽宁商科技集团有限公司 董事长　王力", 32);
        upsertContent("speech_date", "董事长致词-日期", "2026年8月", 33);
        upsertContent("culture_lead", "企业文化-导语", "安徽宁商科技集团生长于合肥科创热土，扎根巢湖之滨。", 40);
        upsertContent("culture_mission", "企业文化-使命", "科创赋能城乡，数字服务实业", 41);
        upsertContent("culture_values", "企业文化-核心价值观", "徽商固本 · 科创致远 · 协同共生 · 开放共赢", 42);
        upsertContent("culture_vision", "企业文化-愿景", "城湖共生处，笃行向远方", 43);
        upsertContent("culture_spirit", "企业文化-精神", "务实笃行 · 创新赋能 · 诚信致远", 44);
        upsertContent("party_title", "党建-标题", "党建领航聚合力 实干奋进启新程", 50);
        upsertContent("party_meta", "党建-副标题", "安徽宁商科技集团筑牢红色根基引领高质量发展", 51);
        upsertContent("party_body", "党建-正文",
                "以高质量党建引领集团高质量发展，传承红色基因，凝聚奋进力量。\n\n一、冲锋在前、实干担当\n安徽宁商科技集团2026年7月启动党支部筹备工作，4名党员全程亮身份、作表率。\n\n二、凝心铸魂、氛围浓厚\n党支部的组织成立过程，是安徽宁商科技集团发展历程中的重要里程碑。\n\n三、深度融合、同频奋进\n站在新的起点，安徽宁商科技集团将以党支部组织成立为契机，始终坚持党建领航。", 52);
        upsertContent("icp_number", "备案号", "皖ICP备2026XXXXXX号-1", 60);
        upsertContent("footer_brand_desc", "底部品牌简介", "安徽宁商科技集团有限公司，立足安徽本土，聚焦科创产业服务，秉持\"务实笃行、创新赋能、诚信致远\"的核心价值观。", 70);
        upsertContent("industry_construction_body", "建筑工程正文", "<p>安徽利至高建设工程有限公司是安徽宁商科技集团旗下建筑工程板块的核心企业，承载集团工程建设与基础设施配套服务的核心职能。</p><p>公司业务覆盖房屋建筑、工业厂房、公共建筑施工总承包，以及城市道路、桥梁、给排水等市政公用工程。</p>", 80);
        upsertContent("industry_software_body", "软件科技正文", "<p>软件科技板块是安徽宁商科技集团数字产业战略的核心引擎，由安徽陆洲科技有限责任公司与安徽合州信息咨询有限责任公司共同构成。</p><p>两家成员企业协同发力，专注AI定制软件开发、卫星信息系统集成及政企数字化转型咨询。</p>", 81);
        upsertContent("recruit_body", "人才理念正文", "<p><strong>务实笃行 · 创新赋能 · 诚信致远</strong></p><p>以人为本，聚才兴企，与志同道合者共赴远方。</p><h3>人才价值观</h3><p><strong>01 务实笃行</strong><br>不尚空谈、不务虚功，以脚踏实地的作风深耕业务。</p><p><strong>02 创新赋能</strong><br>以持续创新的能力响应需求，鼓励探索突破。</p><p><strong>03 诚信致远</strong><br>以诚信合规的经营赢得市场信赖，重信守诺。</p><p><strong>04 开放共赢</strong><br>秉持开放共赢、兼容并蓄的经营理念。</p>", 82);
        upsertContent("bg_about_banner", "集团概况-栏目背景图", "/images/1.jpg", 90);
        upsertContent("bg_about_page", "集团概况-页面大背景图", "/images/about-pic.jpeg", 91);
        upsertContent("bg_news_banner", "新闻中心-栏目背景图", "/images/news-center-banner.jpeg", 92);
        upsertContent("bg_news_page", "新闻中心-页面大背景图", "/images/43B3F7AAFD4D74BF80FA30DFA7B129CE.jpg", 93);
        upsertContent("bg_industry_banner", "集团产业-栏目背景图", "/images/industry-banner.jpeg", 94);
        upsertContent("bg_industry_page", "集团产业-页面大背景图", "/images/2945347C3CC652EA1119F1A7F09DC2A9.jpg", 95);
        upsertContent("bg_contact_banner", "联系宁商-栏目背景图", "/images/contact-banner.jpeg", 96);
        upsertContent("bg_contact_page", "联系宁商-页面大背景图", "/images/b.jpg", 97);
    }

    private void upsertContent(String key, String title, String content, int sortOrder) {
        if (siteContentRepository.findByContentKey(key).isEmpty()) {
            SiteContent sc = new SiteContent();
            sc.setContentKey(key);
            sc.setTitle(title);
            sc.setContent(content);
            sc.setSortOrder(sortOrder);
            siteContentRepository.save(sc);
        }
    }
    private void initNews() {
        News n1 = new News();
        n1.setTitle("安徽宁商科技集团正式成立 扎根合肥赋能区域科创");
        n1.setSummary("安徽宁商科技集团有限公司正式成立，多产业全链条布局完成。");
        n1.setBody("<p>安徽宁商科技集团有限公司正式成立。</p>");
        n1.setCategory("group");
        n1.setCategoryName("集团新闻");
        n1.setDate("2026-07");
        n1.setAuthor("宁商科技集团品牌策划部");
        n1.setSource("内部资料");
        n1.setImage("images/news-establishment.jpeg");
        n1.setNewsId("group-nshang-established");
        newsRepository.save(n1);

        News n2 = new News();
        n2.setTitle("安徽宁商科技集团官方网站正式上线");
        n2.setSummary("近日，安徽宁商科技集团官方网站正式开通运行。");
        n2.setBody("<p>近日，安徽宁商科技集团官方网站正式开通运行。</p>");
        n2.setCategory("group");
        n2.setCategoryName("集团新闻");
        n2.setDate("2026-08");
        n2.setAuthor("宁商科技集团品牌策划部");
        n2.setSource("内部资料");
        n2.setImage("images/news-website-online.jpeg");
        n2.setNewsId("group-website-online");
        newsRepository.save(n2);
    }

    private void initTeamMembers() {
        TeamMember m1 = new TeamMember();
        m1.setName("王 力");
        m1.setPosition("创始人 · 董事长 · 总工程师");
        m1.setDescription("澳大利亚昆士兰大学哲学博士（Ph.D.），集团战略总设计师。");
        m1.setAvatar("images/team-wang-li.jpeg");
        m1.setGradient("linear-gradient(135deg,#1a365d 0%,#2c5282 100%)");
        m1.setSortOrder(1);
        teamMemberRepository.save(m1);
    }

    private void initHonors() {
        String[][] data = {
            {"国家高新技术企业", "申报中，持续攻坚核心自研技术。", "🏅", "images/honor-1.png"},
            {"安徽省软件行业协会会员单位", "深度参与软件行业生态建设。", "💻", "images/honor-2.png"}
        };
        for (int i = 0; i < data.length; i++) {
            Honor h = new Honor();
            h.setTitle(data[i][0]);
            h.setDescription(data[i][1]);
            h.setIcon(data[i][2]);
            h.setImage(data[i][3]);
            h.setSortOrder(i + 1);
            honorRepository.save(h);
        }
    }

    private void initSubsidiaries() {
        String[][] data = {
            {"安徽利至高建设工程有限公司", "CONSTRUCTION ENGINEERING", "建设工程", "深耕工程建设与基础设施配套服务。", "images/sub-logo-4.png", "images/sub-bg-1.png"},
            {"安徽陆洲科技有限责任公司", "TECHNOLOGY", "智能科技", "聚焦智能化配套服务与数字技术应用。", "images/sub-logo-1.png", "images/sub-bg-2.png"}
        };
        for (int i = 0; i < data.length; i++) {
            Subsidiary s = new Subsidiary();
            s.setName(data[i][0]);
            s.setEnglishName(data[i][1]);
            s.setCategory(data[i][2]);
            s.setDescription(data[i][3]);
            s.setLogo(data[i][4]);
            s.setBackground(data[i][5]);
            s.setSortOrder(i + 1);
            subsidiaryRepository.save(s);
        }
    }

    private void initCoreBusinesses() {
        String[][] data = {
            {"建设工程", "建筑施工、市政配套与城市基础设施服务，匠心铸就品质工程。", "images/biz-1.png"},
            {"数字科技", "建筑数字化、人工智能与算法软件开发，打造科创服务核心引擎。", "images/biz-2.png"},
            {"信息咨询", "企业全周期科创赋能与专业咨询，助力规范化高质量发展。", "images/biz-3.png"},
            {"智能装备", "智能装备研发智造与数字技术应用，赋能多领域数字化转型。", "images/biz-4.png"},
            {"物业管理", "物业运营评估与城市综合配套服务，深耕多元城市服务板块。", "images/biz-5.png"}
        };
        for (int i = 0; i < data.length; i++) {
            CoreBusiness b = new CoreBusiness();
            b.setName(data[i][0]);
            b.setDescription(data[i][1]);
            b.setCoverImage(data[i][2]);
            b.setSortOrder(i + 1);
            coreBusinessRepository.save(b);
        }
    }

    private void initMilestones() {
        String[][] data = {
            {"2026.07.28", "宁商集团党支部启动筹备成立", "构建党建引领企业发展体系。"},
            {"2026.07.22", "安徽宁商科技集团正式成立", "多产业全链条布局完成。"}
        };
        for (int i = 0; i < data.length; i++) {
            Milestone m = new Milestone();
            m.setYear(data[i][0]);
            m.setTitle(data[i][1]);
            m.setDescription(data[i][2]);
            m.setSortOrder(i + 1);
            milestoneRepository.save(m);
        }
    }

    private void initPartners() {
        String[][] data = {
            {"安徽利至高建设工程", "/images/sub-logo-0.png", "#", "1"},
            {"安徽陆洲科技", "/images/sub-logo-1.png", "#", "2"},
            {"安徽合州信息咨询", "/images/sub-logo-2.png", "#", "3"},
            {"安徽玉彤智能装备", "/images/sub-logo-3.png", "#", "4"},
            {"合肥南峰建设投资", "/images/sub-logo-4.png", "#", "5"}
        };
        for (String[] item : data) {
            Partner partner = new Partner();
            partner.setName(item[0]);
            partner.setLogo(item[1]);
            partner.setLink(item[2]);
            partner.setStatus(1);
            partner.setSortOrder(Integer.parseInt(item[3]));
            partnerRepository.save(partner);
        }
    }
    // ==================== 后台菜单初始化 ====================

    private void initMenus() {
        mergeDuplicateRootGroups("集团概况");
        removeObsoleteMenus();
        AdminMenu about = ensureGroup("集团概况", "OfficeBuilding", 1);
        AdminMenu news = ensureGroup("新闻管理", "Document", 2);
        AdminMenu industry = ensureGroup("集团产业", "Grid", 3);
        AdminMenu interaction = ensureGroup("联系宁商", "Service", 4);
        AdminMenu system = ensureGroup("系统管理", "Tools", 5);

        ensureMenu(about, "集团简介", "/ningshang-admin/content/intro", "OfficeBuilding", 1);
        ensureMenu(about, "董事长致词", "/ningshang-admin/content/speech", "ChatLineSquare", 2);
        ensureMenu(about, "发展大事记", "/ningshang-admin/milestones", "Clock", 3);
        ensureMenu(about, "团队管理", "/ningshang-admin/team", "User", 4);
        ensureMenu(about, "企业荣誉", "/ningshang-admin/honors", "Trophy", 5);
        ensureMenu(about, "企业文化", "/ningshang-admin/content/culture", "Flag", 6);
        ensureMenu(about, "党建工作", "/ningshang-admin/content/party", "Star", 7);
        ensureMenu(about, "合作伙伴", "/ningshang-admin/partners", "Connection", 8);
        ensureMenu(news, "新闻列表", "/ningshang-admin/news", "Document", 1);
        ensureMenu(industry, "子公司管理", "/ningshang-admin/subsidiaries", "OfficeBuilding", 1);
        ensureMenu(interaction, "人才理念", "/ningshang-admin/content/recruit", "User", 1);
        ensureMenu(interaction, "招聘岗位", "/ningshang-admin/jobs", "Briefcase", 2);
        ensureMenu(interaction, "在线留言", "/ningshang-admin/messages", "ChatDotRound", 3);
        ensureMenu(interaction, "联系方式", "/ningshang-admin/chatline", "Document", 4);
        ensureMenu(system, "菜单管理", "/ningshang-admin/menus", "Menu", 1);
        ensureMenu(system, "管理员分组", "/ningshang-admin/groups", "Avatar", 2);
        ensureMenu(system, "管理员账号", "/ningshang-admin/admins", "UserFilled", 3);
        ensureMenu(system, "网站设置", "/ningshang-admin/site-settings", "Setting", 4);
    }

    private void removeObsoleteMenus() {
        java.util.Set<Long> removeIds = new java.util.LinkedHashSet<>();
        for (String path : java.util.List.of(
                "/ningshang-admin/content/home",
                "/ningshang-admin/content/other",
                "/ningshang-admin/content/construction",
                "/ningshang-admin/content/software",
                "/ningshang-admin/core-businesses")) {
            adminMenuRepository.findByPath(path).ifPresent(menu -> removeIds.add(menu.getId()));
        }
        for (String groupName : java.util.List.of("首页与全站", "内容管理", "内容运营")) {
            adminMenuRepository.findByParentIdAndName(0L, groupName).ifPresent(group -> {
                for (AdminMenu child : adminMenuRepository.findByParentIdOrderBySortOrderAsc(group.getId())) {
                    removeIds.add(child.getId());
                }
                removeIds.add(group.getId());
            });
        }
        if (!removeIds.isEmpty()) {
            java.util.List<Long> ids = new java.util.ArrayList<>(removeIds);
            adminGroupMenuRepository.deleteByMenuIdIn(ids);
            adminMenuRepository.deleteAllById(ids);
        }
    }

    private void mergeDuplicateRootGroups(String name) {
        java.util.List<AdminMenu> duplicates = adminMenuRepository.findByParentIdOrderBySortOrderAsc(0L).stream()
                .filter(menu -> name.equals(menu.getName())).collect(java.util.stream.Collectors.toList());
        if (duplicates.size() < 2) return;
        AdminMenu keeper = duplicates.get(0);
        java.util.List<Long> removeIds = new java.util.ArrayList<>();
        for (int i = 1; i < duplicates.size(); i++) {
            AdminMenu duplicate = duplicates.get(i);
            for (AdminMenu child : adminMenuRepository.findByParentIdOrderBySortOrderAsc(duplicate.getId())) {
                child.setParentId(keeper.getId());
                adminMenuRepository.save(child);
            }
            removeIds.add(duplicate.getId());
        }
        adminGroupMenuRepository.deleteByMenuIdIn(removeIds);
        adminMenuRepository.deleteAllById(removeIds);
    }

    private AdminMenu ensureGroup(String name, String icon, int sortOrder) {
        AdminMenu group = adminMenuRepository.findByParentIdAndName(0L, name).orElseGet(() -> {
            // 复用旧分组，避免产生重复导航。
            if ("内容管理".equals(name)) return adminMenuRepository.findByParentIdAndName(0L, "内容运营").orElse(new AdminMenu());
            if ("首页与全站".equals(name)) return adminMenuRepository.findByParentIdAndName(0L, "内容管理").orElseGet(() -> adminMenuRepository.findByParentIdAndName(0L, "内容运营").orElse(new AdminMenu()));
            if ("集团概况".equals(name)) return adminMenuRepository.findByParentIdAndName(0L, "企业资料").orElse(new AdminMenu());
            if ("联系宁商".equals(name)) return adminMenuRepository.findByParentIdAndName(0L, "人才与互动").orElse(new AdminMenu());
            if ("系统管理".equals(name)) return adminMenuRepository.findByParentIdAndName(0L, "系统设置").orElse(new AdminMenu());
            return new AdminMenu();
        });
        group.setParentId(0L); group.setName(name); group.setPath(null); group.setIcon(icon);
        group.setSortOrder(sortOrder); group.setStatus(1);
        return adminMenuRepository.save(group);
    }

    private AdminMenu ensureMenu(AdminMenu parent, String name, String path, String icon, int sortOrder) {
        AdminMenu menu = adminMenuRepository.findByPath(path).orElse(new AdminMenu());
        menu.setParentId(parent.getId()); menu.setName(name); menu.setPath(path); menu.setIcon(icon);
        menu.setSortOrder(sortOrder); menu.setStatus(1);
        return adminMenuRepository.save(menu);
    }

    private AdminMenu createMenu(Long parentId, String name, String path, String icon, int sortOrder) {
        AdminMenu m = new AdminMenu();
        m.setParentId(parentId);
        m.setName(name);
        m.setPath(path);
        m.setIcon(icon);
        m.setSortOrder(sortOrder);
        m.setStatus(1);
        return adminMenuRepository.save(m);
    }

    // ==================== 权限组初始化 ====================

    private void initGroups() {
        if (adminGroupRepository.count() > 0) return;

        createGroup("超级管理员", "拥有所有权限", 1);
        createGroup("管理员", "内容管理权限", 2);
        createGroup("编辑", "仅新闻管理", 3);
    }

    private void createGroup(String name, String desc, int sortOrder) {
        AdminGroup g = new AdminGroup();
        g.setName(name);
        g.setDescription(desc);
        g.setStatus(1);
        adminGroupRepository.save(g);
    }

    // ==================== 组-菜单关联初始化 ====================

    private void initGroupMenus() {
        java.util.List<AdminMenu> allMenus = adminMenuRepository.findAll();
        java.util.Set<Long> allMenuIds = allMenus.stream().map(AdminMenu::getId).collect(java.util.stream.Collectors.toSet());

        if (adminGroupMenuRepository.count() > 0) {
            // 菜单重新分组后，为已有角色补齐其已授权子菜单的新父级。
            for (AdminGroup group : adminGroupRepository.findAll()) {
                java.util.Set<Long> assigned = new java.util.HashSet<>(adminGroupMenuRepository.findMenuIdByGroupId(group.getId()));
                if ("管理员".equals(group.getName())) {
                    for (AdminMenu menu : allMenus) {
                        String path = menu.getPath();
                        if (path != null && !path.startsWith("/ningshang-admin/menus")
                                && !path.startsWith("/ningshang-admin/groups")
                                && !path.startsWith("/ningshang-admin/admins") && !assigned.contains(menu.getId())) {
                            bindGroupMenu(group.getId(), menu.getId());
                            assigned.add(menu.getId());
                        }
                    }
                }
                for (AdminMenu menu : allMenus) {
                    if (assigned.contains(menu.getId()) && menu.getParentId() != null && menu.getParentId() > 0 && !assigned.contains(menu.getParentId())) {
                        bindGroupMenu(group.getId(), menu.getParentId());
                        assigned.add(menu.getParentId());
                    }
                }
            }
            return;
        }

        // 超级管理员组 → 所有菜单
        java.util.Optional<AdminGroup> superGroup = adminGroupRepository.findByName("超级管理员");
        if (superGroup.isPresent()) {
            for (Long menuId : allMenuIds) {
                bindGroupMenu(superGroup.get().getId(), menuId);
            }
        }

        // 管理员组 → 内容管理 + 网站设置
        java.util.Optional<AdminGroup> adminGroup = adminGroupRepository.findByName("管理员");
        if (adminGroup.isPresent()) {
            for (AdminMenu m : allMenus) {
                String path = m.getPath();
                if (path != null && !path.startsWith("/ningshang-admin/menus")
                        && !path.startsWith("/ningshang-admin/groups")
                        && !path.startsWith("/ningshang-admin/admins")) {
                    bindGroupMenu(adminGroup.get().getId(), m.getId());
                }
            }
        }

        // 编辑组 → 仅新闻管理
        java.util.Optional<AdminGroup> editorGroup = adminGroupRepository.findByName("编辑");
        if (editorGroup.isPresent()) {
            for (AdminMenu m : allMenus) {
                if ("/ningshang-admin/news".equals(m.getPath())) {
                    bindGroupMenu(editorGroup.get().getId(), m.getId());
                }
            }
        }
    }

    private void bindGroupMenu(Long groupId, Long menuId) {
        AdminGroupMenu r = new AdminGroupMenu();
        r.setGroupId(groupId);
        r.setMenuId(menuId);
        adminGroupMenuRepository.save(r);
    }

    private void initPermissions() {
        String[][] modules = {{"news","新闻管理"},{"content","页面内容"},{"subsidiaries","子公司管理"},{"partners","合作伙伴"},{"core-businesses","核心业务领域"},{"team","团队管理"},{"honors","荣誉管理"},{"milestones","大事记管理"},{"jobs","招聘管理"},{"messages","留言管理"},{"menus","菜单管理"},{"groups","管理员分组"},{"admins","管理员账号"}};
        String[][] actions = {{"list","查看列表"},{"create","新增"},{"update","修改"},{"delete","删除"},{"batch_delete","批量删除"}};
        int order = 0;
        for (String[] m : modules) for (String[] a : actions) {
            if (m[0].equals("messages") && (a[0].equals("create") || a[0].equals("update"))) continue;
            String code = m[0] + ":" + a[0];
            AdminPermission p = adminPermissionRepository.findByCode(code).orElse(new AdminPermission());
            p.setModule(m[0]); p.setModuleName(m[1]); p.setAction(a[0]); p.setCode(code); p.setName(a[1]); p.setSortOrder(++order); p.setStatus(1);
            adminPermissionRepository.save(p);
        }
        for (AdminGroup g : adminGroupRepository.findAll()) {
            for (AdminPermission p : adminPermissionRepository.findAll()) {
                boolean grant = Long.valueOf(1).equals(g.getId()) || ("管理员".equals(g.getName()) && !java.util.Set.of("menus","groups","admins").contains(p.getModule())) || ("编辑".equals(g.getName()) && "news".equals(p.getModule()));
                if (grant && !adminGroupPermissionRepository.existsByGroupIdAndPermissionId(g.getId(), p.getId())) { AdminGroupPermission gp = new AdminGroupPermission(); gp.setGroupId(g.getId()); gp.setPermissionId(p.getId()); adminGroupPermissionRepository.save(gp); }
            }
        }
    }

    // ==================== 默认管理员关联超级组 ====================

    private void linkDefaultAdminToSuperGroup() {
        java.util.Optional<AdminGroup> superGroup = adminGroupRepository.findByName("超级管理员");
        if (superGroup.isEmpty()) return;
        adminRepository.findByUsername("admin").ifPresent(admin -> {
            if (admin.getGroupId() == null) {
                admin.setGroupId(superGroup.get().getId());
                adminRepository.save(admin);
            }
        });
    }
}

