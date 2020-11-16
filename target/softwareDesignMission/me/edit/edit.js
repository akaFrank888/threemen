"use strict";
function changeUserInfoHandler() {
    const change_pw_panel = document.querySelector('.change_pw_panel');
    change_pw_panel.addEventListener('click', (e) => {
        e.stopPropagation();
    });
    const change_pw_form = document.getElementById('change_pw');
    function makeRequest() {
        const requestURL = '/userInfo/saveUserInfo';
        const form_data = new FormData(change_pw_form);
        const urlParams = new URLSearchParams();
        for (const data of form_data) {
            urlParams.append(data[0], data[1]);
        }
        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }
    change_pw_form.addEventListener('submit', (e) => {
        e.preventDefault();
        makeRequest().then((obj) => {
            if (obj['flag']) {
                alert(obj['errorMsg']);
                window.location.replace('/me/');
            }
            else {
                alert(obj['errorMsg']);
            }
        }).catch(console.log);
    });
}
function verificationHandler() {
    const formSelf = document.getElementById('verify_form');
    function makeRequest() {
        const requestURL = '/userInfo/userRealize';
        const formData = new FormData(formSelf);
        const urlParams = new URLSearchParams();
        for (const data of formData) {
            urlParams.append(data[0], data[1]);
        }
        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }
    formSelf.addEventListener('submit', (e) => {
        e.preventDefault();
        makeRequest().then((obj) => {
            if (obj['flag']) {
                alert('认证通过，请继续填写个人信息');
                hideVerifyAndShowInfoPanel();
                history.replaceState(null, '', window.location.pathname);
            }
            else {
                alert('认证失败，请重试。' + obj['errorMsg']);
            }
        }).catch(console.log);
    });
}
function hideVerifyAndShowInfoPanel() {
    const verify_panel = document.querySelector('.verify_panel');
    const edit_panel = document.querySelector('.change_pw_panel');
    verify_panel.classList.add('hide');
    edit_panel.classList.remove('hide');
}
// check if redirected from registeration page
// if true, hide edit panel and show verify panel
// else hide verify_panel and show edit panel
// by manipulating class 'hide'
// class hide is in html by default
function checkIfIsFromRegisterationPage() {
    const verify_panel = document.querySelector('.verify_panel');
    const edit_panel = document.querySelector('.change_pw_panel');
    if (window.location.hash.substring(1) === 'register') {
        verify_panel.classList.remove('hide');
        verificationHandler();
    }
    else {
        edit_panel.classList.remove('hide');
    }
    changeUserInfoHandler();
}
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', checkIfIsFromRegisterationPage);
}
else {
    checkIfIsFromRegisterationPage();
}
