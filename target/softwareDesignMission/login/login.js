"use strict";

function submitHandler() {
    const form_self = document.querySelector('.form');
    const submit_btn = document.querySelector('.submit_btn');
    function submitCheck() {
        if (document.getElementById('account').value != ''
            && document.getElementById('password').value != '') {
            return true;
        } else {
            setTimeout(alert('账号和密码不能为空！'));
            return false;
        }
    }

    function submit() {
        const requestURL = '/user/login';
        let form_data = new FormData(form_self);
        let url_params = new URLSearchParams();

        for (let pair of form_data) {
            url_params.append(pair[0], pair[1]);
        }

        return fetch(requestURL, {
            method: 'POST',
            body: url_params,
            credentials: "same-origin"
        }).then(res => res.json()).catch(console.log);
    }

    form_self.addEventListener('click', e => {
        if (e.target === submit_btn) {
            e.preventDefault();
            if (submitCheck()) {
                submit().then(data => {
                    if (data['flag']) {
                        // setTimeout(alert(data['errorMsg']));
                        if (document.getElementById('autologin').checked) {
                            fetch('/user/saveLogin', { method: "POST", credentials: "same-origin" });
                        }
                        location.replace('/');
                    } else {
                        // TODO: 重做提醒
                        setTimeout(alert(data['errorMsg']));
                    }
                }).catch(console.log); // TODO: 处理返回值
            }
        }
    });
}



if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        submitHandler();
    });
} else {
    submitHandler();
}