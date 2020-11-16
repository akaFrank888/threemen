"use strict";
// insert header to body, and set link style
function insertHeader() {
    // set header dom, and add active class to section link
    function setHeaderDOM() {
        const header = `<header>
<div class="header_wrapper">
    <input type="checkbox" name="show_list" id="show_list" class="show_list visually_hidden">
    <label for="show_list" class="show_list_label">展开列表</label>
    <div class="logo"><a href="/" class="logo_link"><img src="/static/img/three_men_transparent.png" alt="Three men logo"
                class="logo_img"></a></div>
    <ul class="section_list">
        <h1 class="visually_hidden list_title">区域</h1>
        <li class="list_item"><a href="/" class="item_link">首页</a></li>
        <li class="list_item"><a href="/search/" class="item_link">兼职</a></li>
        <li class="list_item"><a href="/ordering_food/" class="item_link">食堂订餐</a></li>
        <li class="list_item"><a href="/shopping/buy/" class="item_link">超市代购</a></li>
        <li class="list_item"><a href="/logistic/buy/" class="item_link">快递帮拿</a></li>
    </ul>
    <section class="account_section login_and_register">
    <!-- <section class="account_section login_and_register hidden"> -->
        <a href="/login/" class="login account_func">登录</a>
        <a href="/register/" class="register account_func">注册</a>
    </section>
    <section class="account_section with_avatar hidden">
    <!-- <section class="account_section with_avatar"> -->
        <a href="/me/" class="account">
            <img src="/static/img/default_avatar.png" alt="User avatar" class="avatar">
        </a>
        <div class="avatar_panel">
        <!-- <div class="avatar_panel" style="opacity: 1; pointer-events: all;"> -->
            <div class="avatar_name_wrapper">
                <a href="/me/" class="avatar">
                    <img src="/static/img/default_avatar.png" alt="User avatar" class="avatar_img">
                </a>
                <h1 class="nickname">用户名</h1>
            </div>
            <div class="func_list_wrapper">
                <ul class="func_list">
                    <li class="list_item"><a href="#" class="item_link" id="balance">金子余额<span class="balance">0</span></a></li>
                    <li class="list_item"><a href="#" class="item_link" id="recharge">金子充值</a></li>
                    <li class="list_item"><a href="#" class="item_link" id="withdraw">金子提现</a></li>
                    <li class="list_item"><a href="/me/order/" class="item_link" id="order">我的订单</a></li>
                    <li class="list_item"><a href="/me/" class="item_link" id="verify">兼职认证</a></li>
                    <li class="list_item"><a href="#" class="item_link logout" id="logout">退出登录</a></li>
                </ul>
            </div>
        </div>
    </section>
</div>
</header>
`;
        document.body.innerHTML = header + document.body.innerHTML;
        const section_links = document.querySelectorAll('.section_list .item_link');
        section_links.forEach((ele) => {
            if (window.location.pathname === (ele.pathname)) {
                ele.classList.add('active');
            }
        });
    }
    // insert link element into head
    function insertStyleSheet(path, css) {
        if (!path.endsWith('/')) {
            path += '/';
        }
        const link_css = document.querySelector('link[rel=stylesheet]');
        for (const str of css) {
            const item = document.createElement('link');
            item.rel = 'stylesheet';
            item.href = path + str + '.css';
            document.head.insertBefore(item, link_css);
        }
    }
    // handle mobi list behaviour
    function showListMobi() {
        const show_list_checkbox = document.querySelector('.show_list');
        const section_list = document.querySelector('.section_list');
        const removeClass = () => {
            section_list.classList.remove('animated');
            section_list.removeEventListener('transitionend', removeClass);
        };
        const removeClassCanceled = () => {
            if (show_list_checkbox.checked) {
                section_list.removeEventListener('transitionend', removeClass);
            }
        };
        show_list_checkbox.addEventListener('change', () => {
            if (show_list_checkbox.checked) {
                section_list.classList.add('animated');
            }
            else {
                section_list.addEventListener('transitionend', removeClass);
            }
        });
        section_list.addEventListener('transitioncancel', removeClassCanceled);
    }
    const STATIC_CSS_PATH = '/static/css/';
    const css = ['normalize', 'header', 'header_mobi'];
    insertStyleSheet(STATIC_CSS_PATH, css);
    setHeaderDOM();
    showListMobi();
}
// insert common script
function insertScript(path, js) {
    if (!path.endsWith('/')) {
        path += '/';
    }
    for (const str of js) {
        const item = document.createElement('script');
        item.src = path + str + '.js';
        document.body.appendChild(item);
    }
}
function init_page() {
    insertHeader();
    const STATIC_JS_PATH = '/static/js/';
    const js = ['account'];
    insertScript(STATIC_JS_PATH, js);
}
init_page();
// if (document.readyState === 'loading') {
//     document.addEventListener('DOMContentLoaded', init_page);
// } else {
//     init_page();
// }
