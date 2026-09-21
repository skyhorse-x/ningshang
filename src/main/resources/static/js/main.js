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
// 说明：旧静态站曾在 JS 里硬编码整份头部/页脚，并在页面加载后覆盖服务端渲染的导航，
// 导致导航链接退回 industry.html / about-intro.html 等旧路径（下级菜单也停在过期内容）。
// 现已移除，头部与页脚一律由 Thymeleaf fragments 服务端渲染。

(function () {
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

  // 留言表单：真正提交到 POST /api/messages（之前只弹 alert，留言进不了后台）
  function bindForm() {
    var form = document.querySelector('form.contact-form');
    if (!form) return;
    form.addEventListener('submit', function (e) {
      e.preventDefault();
      var btn = form.querySelector('button[type="submit"]');
      var payload = {
        name: (form.name.value || '').trim(),
        phone: (form.phone.value || '').trim(),
        email: (form.email.value || '').trim(),
        type: form.type.value || 'other',
        content: (form.message.value || '').trim()
      };
      if (!payload.name || !payload.phone || !payload.content) {
        alert('请填写姓名、联系电话和留言内容');
        return;
      }
      var originalText = btn ? btn.textContent : '';
      if (btn) { btn.disabled = true; btn.textContent = '提交中…'; }
      function restore() { if (btn) { btn.disabled = false; btn.textContent = originalText || '提交留言'; } }
      fetch('/api/messages', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      }).then(function (res) {
        return res.json().catch(function () { return {}; });
      }).then(function (res) {
        if (res && res.code === 200) {
          alert('感谢您的留言，我们已收到信息，将尽快与您联系！');
          form.reset();
        } else {
          alert((res && res.message) || '提交失败，请稍后重试');
        }
        restore();
      }).catch(function () {
        alert('网络异常，提交失败，请稍后重试');
        restore();
      });
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

