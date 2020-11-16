"use strict";
function submitHandler() {
    const form_self = document.querySelector('.form');
    const submit_btn = document.querySelector('.submit_btn');
    function submitCheck() {
        if (document.getElementById('account').value != ''
            && document.getElementById('password').value != '') {
            return true;
        } else {
            alert('账号和密码不能为空！');
            return false;
        }
    }

    function submit() {
        const requestURL = '/user/register';
        let form_data = new FormData(form_self);
        let url_params = new URLSearchParams();

        for (let pair of form_data) {
            url_params.append(pair[0], pair[1]);
        }

        return fetch(requestURL, {
            method: 'POST',
            body: url_params,
            credentials: "same-origin"
        })
            .then(res => res.json())
            .catch(console.log);
    }
    form_self.addEventListener('click', e => {
        if (e.target === submit_btn) {
            e.preventDefault();
            if (submitCheck()) {
                submit()
                .then(data => {
                    if (data['flag']) {
                        location.replace('/');
                        // TODO 重写提醒
                        alert('注册成功！请登录邮箱进行账户验证。');
                    } else {
                        alert(data['errorMsg'] + " 请重试。");
                    }
                }).catch(console.log); // TODO: 处理返回值
            }
        }
    });
}

function verificationCodeHandler() {
    function getVerificationCode(target) {
        const requestURL = '/checkCode' + '?' + new Date().getTime();
        target.src = requestURL;
    }
    const verificationCodeImg = document.querySelector('.securityCode_img');

    verificationCodeImg.addEventListener('click', () => {
        getVerificationCode(verificationCodeImg);
    });
    getVerificationCode(verificationCodeImg);
}

function fixEmailAnimation() {
    const input = document.querySelector('.email_input');
    const text = document.querySelector('.email_label .inline_text');
    input.addEventListener('focus', () => {
        text.classList.add('active');
    });

    input.addEventListener('blur', () => {
        if (input.value === '') {
            text.classList.remove('active');
        }
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        submitHandler();
        verificationCodeHandler();
        fixEmailAnimation();
    });
} else {
    submitHandler();
    verificationCodeHandler();
    fixEmailAnimation();
}

