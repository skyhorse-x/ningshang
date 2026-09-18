/* ===== 宁商科技集团 官网公共脚本 ===== */
// 站点生成的横幅图（SDXL）
var IMG = {
  hero1: "images/hero-1.jpeg?v=20260825",
  hero2: "images/hero-2.jpg?v=20260825",
  hero3: "images/hero-3.jpg?v=20260825",
  aboutBanner: "images/1.jpg?v=20260902",
  aboutPic: "images/about-pic.jpeg",
  newsBanner: "images/news-center-banner.jpeg",
  industryBanner: "images/industry-banner.jpeg",
  contactBanner: "images/contact-banner.jpeg",
  recruitBanner: "images/news-staff-training.jpeg",
  eventsBanner: "images/dashiji.jpg",
  partyBanner: "images/dangjian.jpg",
  subBg1: "images/sub-bg-1.png",
  subBg2: "images/sub-bg-2.png",
  subBg3: "images/sub-bg-3.png",
  subBg4: "images/sub-bg-4.png",
  subBg5: "images/sub-bg-5.png"
};


//           <li><a href="news.html#tender">招标公告</a></li> 暂无投标 暂定删除
//           <li><a href="news.html#magazine">宁商内刊</a></li> 暂无内刊 暂定删除
// ===== 头部 HTML =====
var HEADER_HTML = '\
<div class="topbar">\
  <div class="wrap">\
    <div class="tl">\
      <span><i>✆</i>服务热线：13365745652</span>\
      <span><i>✉</i>NStechnology@163.com</span>\
    </div>\
    <div class="tr">\
      <a href="recruit.html">人才招聘</a>\
      <a href="contact.html">联系宁商</a>\
    </div>\
  </div>\
</div>\
<div class="header">\
  <div class="wrap">\
    <a href="index.html" class="brand">\
      <img src="images/logo.png" alt="安徽宁商科技集团" width="72" height="54">\
      <span class="bt"><b>宁商科技集团</b><small>ANHUI NINGSHANG TECH GROUP</small></span>\
    </a>\
    <div class="menu-btn"><span></span><span></span><span></span></div>\
    <ul class="nav">\
      <li><a href="index.html">首页<span class="en">HOME</span></a></li>\
      <li><a href="about-intro.html">集团概况<span class="en">ABOUT US</span></a>\
        <ul class="subnav">\
          <li><a href="about-intro.html">集团简介</a></li>\
          <li><a href="about-speech.html">董事长致词</a></li>\
          <li><a href="about-events.html">发展大事记</a></li>\
          <li><a href="about-team.html">管理团队</a></li>\
          <li><a href="about-honor.html">企业荣誉</a></li>\
          <li><a href="about-party.html">党建工作</a></li>\
          <li><a href="about-culture.html">企业文化</a></li>\
        </ul>\
      </li>\
      <li><a href="news.html">新闻中心<span class="en">NEWS</span></a>\
        <ul class="subnav">\
          <li><a href="news.html#group">集团新闻</a></li>\
          <li><a href="news.html#industry">产业动态</a></li>\
          <li><a href="news.html#trend">行业资讯</a></li>\
          <li><a href="news.html#staff">员工风采</a></li>\
        </ul>\
      </li>\
      <li><a href="industry.html">集团产业<span class="en">INDUSTRY</span></a>\
        <ul class="subnav">\
          <li><a href="industry-construction.html">建筑工程</a></li>\
          <li><a href="industry-software.html">软件科技</a></li>\
        </ul>\
      </li>\
      <li><a href="contact.html">联系宁商<span class="en">CONTACT</span></a>\
        <ul class="subnav">\
          <li><a href="recruit.html">人才理念</a></li>\
          <li><a href="recruit-job.html">招聘岗位</a></li>\
          <li><a href="contact.html">联系方式</a></li>\
          <li><a href="contact-message.html">在线留言</a></li>\
        </ul>\
      </li>\
    </ul>\
  </div>\
</div>';

// 公众号模块已下线，如需恢复：
// <div class="qr"><div class="box">官方微信</div>关注公众号</div>
// ===== 页脚 HTML =====
var FOOTER_HTML = '\
<div class="footer">\
  <div class="wrap">\
    <div class="grid">\
      <div class="fbrand">\
        <img src="images/logo.png" alt="安徽宁商科技集团" width="72" height="54">\
        <p>安徽宁商科技集团有限公司，立足安徽本土，聚焦科创产业服务，秉持“务实笃行、创新赋能、诚信致远”的核心价值观，践行“城湖共生处，笃行向远方”的发展愿景。</p>\
      </div>\
      <div>\
        <h6>集团概况</h6>\
        <ul class="flink">\
          <li><a href="about-intro.html">集团简介</a></li>\
          <li><a href="about-speech.html">董事长致词</a></li>\
          <li><a href="about-events.html">发展大事记</a></li>\
          <li><a href="about-team.html">管理团队</a></li>\
          <li><a href="about-honor.html">企业荣誉</a></li>\
          <li><a href="about-party.html">党建工作</a></li>\
          <li><a href="about-culture.html">企业文化</a></li>\
        </ul>\
      </div>\
      <div>\
        <h6>快捷导航</h6>\
        <ul class="flink">\
          <li><a href="news.html">新闻中心</a></li>\
          <li><a href="industry.html">集团产业</a></li>\
          <li><a href="recruit.html">人才理念</a></li>\
          <li><a href="contact.html">联系方式</a></li>\
          <li><a href="contact-message.html">在线留言</a></li>\
        </ul>\
      </div>\
      <div class="fcontact">\
        <h6>联系方式</h6>\
        <ul>\
          <li>📍 安徽省合肥市蜀山区24号创新工场</li>\
          <li>✆ 13365745652</li>\
          <li>✉ NStechnology@163.com</li>\
        </ul>\
      </div>\
    </div>\
  </div>\
  <div class="copy">Copyright © 2026 安徽宁商科技集团有限公司 皖ICP备2026XXXXXX号-1　技术支持：宁商科技</div>\
</div>';

(function () {
  // 注入头部、页脚
  function inject() {
    var h = document.getElementById('site-header');
    var f = document.getElementById('site-footer');
    if (h) h.innerHTML = HEADER_HTML;
    if (f) f.innerHTML = FOOTER_HTML;
  }

  // 导航高亮
  function activeNav() {
    var path = location.pathname.split('/').pop() || 'index.html';
    var links = document.querySelectorAll('.nav > li');
    links.forEach(function (li) {
      var a = li.querySelector('a');
      if (!a) return;
      var href = a.getAttribute('href') || '';
      if (href.indexOf(path) === 0) li.classList.add('on');
      li.querySelectorAll('.subnav a').forEach(function (sa) {
        var sh = sa.getAttribute('href') || '';
        var tgt = sh.split('#')[0];
        if (tgt === path) li.classList.add('on');
      });
    });
  }

  // 移动端菜单
  function bindMenu() {
    var btn = document.querySelector('.menu-btn');
    var nav = document.querySelector('.nav');
    if (!btn || !nav) return;
    btn.addEventListener('click', function () {
      nav.classList.toggle('open');
    });
    document.querySelectorAll('.nav > li').forEach(function (li) {
      var a = li.querySelector('a');
      var sub = li.querySelector('.subnav');
      if (a && sub) {
        a.addEventListener('click', function (e) {
          if (window.innerWidth <= 1000) {
            e.preventDefault();
            li.classList.toggle('open');
          }
        });
      }
    });
  }

  // 首页轮播
  function heroCarousel() {
    var hero = document.querySelector('.hero');
    if (!hero) return;
    var slides = hero.querySelectorAll('.slide');
    var dots = hero.querySelectorAll('.dots span');
    if (slides.length < 2) return;
    var idx = 0, timer;

    function go(i) {
      slides[idx].classList.remove('on');
      if (dots[idx]) dots[idx].classList.remove('on');
      idx = (i + slides.length) % slides.length;
      slides[idx].classList.add('on');
      if (dots[idx]) dots[idx].classList.add('on');
    }

    timer = setInterval(function () {
      go(idx + 1);
    }, 5500);
    dots.forEach(function (d, i) {
      d.addEventListener('click', function () {
        clearInterval(timer);
        go(i);
        timer = setInterval(function () {
          go(idx + 1);
        }, 5500);
      });
    });
  }

  // 新闻分类筛选
  function newsFilter() {
    var tabs = document.querySelectorAll('.news-tabs a');
    var items = document.querySelectorAll('.news-waterfall .nitem');
    if (!tabs.length) return;

    function apply(cat) {
      tabs.forEach(function (t) {
        t.classList.toggle('on', t.getAttribute('data-cat') === cat);
      });
      items.forEach(function (it) {
        it.style.display = (cat === 'all' || it.getAttribute('data-cat') === cat) ? '' : 'none';
      });
    }

    var hash = (location.hash || '').replace('#', '');
    apply(hash || 'all');
    tabs.forEach(function (t) {
      t.addEventListener('click', function () {
        apply(t.getAttribute('data-cat'));
      });
    });
  }

  // 首页新闻Tab筛选：和 Vue 首页保持一致，默认显示集团新闻，每个栏目只显示 5 条。
  function homeNewsTabs() {
    var tabs = document.querySelectorAll('.hn-tabs a');
    var items = Array.prototype.slice.call(document.querySelectorAll('.hn-items li'));
    if (!tabs.length) return;

    function apply(cat) {
      var shown = 0;
      tabs.forEach(function (t) {
        t.classList.toggle('on', t.getAttribute('data-cat') === cat);
      });
      items.forEach(function (it) {
        var match = it.getAttribute('data-cat') === cat;
        it.style.display = match && shown < 5 ? '' : 'none';
        if (match) shown += 1;
      });
    }

    tabs.forEach(function (t) {
      t.addEventListener('click', function (e) {
        e.preventDefault();
        apply(t.getAttribute('data-cat'));
      });
    });
    var active = document.querySelector('.hn-tabs a.on') || tabs[0];
    apply(active.getAttribute('data-cat'));
  }


  function bindHomeVideo() {
    var open = document.querySelector('[data-video-open]');
    var modal = document.getElementById('home-video-modal');
    var close = document.querySelector('[data-video-close]');
    var video = document.getElementById('home-video-player');
    if (!open || !modal) return;
    function hide() { modal.classList.remove('open'); modal.setAttribute('aria-hidden', 'true'); if (video) video.pause(); }
    open.addEventListener('click', function () { modal.classList.add('open'); modal.setAttribute('aria-hidden', 'false'); if (video) video.play().catch(function () {}); });
    if (close) close.addEventListener('click', hide);
    modal.addEventListener('click', function (e) { if (e.target === modal) hide(); });
  }

  // 表单反馈
  function bindForm() {
    var form = document.querySelector('form.contact-form');
    if (!form) return;
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      alert('感谢您的留言，我们已收到信息，将尽快与您联系！');
      form.reset();
    });
  }

  // 给带 data-bg 的元素设置背景图
  function applyBg() {
    document.querySelectorAll('[data-bg]').forEach(function (el) {
      var key = el.getAttribute('data-bg');
      if (IMG[key]) el.style.backgroundImage = "url('" + IMG[key] + "')";
    });
    document.querySelectorAll('[data-img]').forEach(function (el) {
      var key = el.getAttribute('data-img');
      if (IMG[key]) el.src = IMG[key];
    });
  }

  document.addEventListener('DOMContentLoaded', function () {
    inject();
    activeNav();
    bindMenu();
    heroCarousel();
    newsFilter();
    homeNewsTabs();
    bindHomeVideo();
    bindForm();
    applyBg();



  });
})();

