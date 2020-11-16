"use strict";
function accountHandler() {
    // 已登录返回true，未登录返回false，错误返回undefined
    function checkIfLogon() {
        const requestURL = '/user/findUser';

        return fetch(requestURL, {
            method: "POST",
            credentials: "same-origin"
        })
            .then(res => res.json())
            .then(data => {
                if (data) {
                    return data['flag'] ? data['flag'] : console.log(data['errorMsg']);
                }
            }).catch(console.log);
    }

    function getUserImgObj() {
        const requestURL = '/userInfo/showImg';

        return fetch(requestURL, {
            method: "POST",
            credentials: "same-origin"
        }).then(res => res.json()).catch(console.log);
    }

    let swap_img = () => {
        const default_dir = '/static/img/default_avatar.png';
        return getUserImgObj().then(res => {
            let data;
            const avatar_header = document.querySelector('.avatar');
            const avatar_panel = document.querySelector('.avatar_img');
            if (res['dataObj']) {
                data = res['dataObj']; // Base64
                avatar_header.src = 'data:image/png;base64,' + data;
                avatar_panel.src = 'data:image/png;base64,' + data;
            } else { // fallback img
                avatar_header.src = default_dir;
                avatar_panel.src = default_dir;
            }
        }).catch(console.log);
    };

    function avatar_panel_handler() {
        let get_user_info_obj = () => {
            const requestURL = '/userInfo/showUserInfo';

            return fetch(requestURL, {
                method: 'POST',
                credentials: 'same-origin'
            }).then(res => res.json()).catch(console.log);
        };

        let get_user_balance_obj = () => {
            const requestURL = '/userSelf/showUserSelf';

            return fetch(requestURL, {
                method: 'POST',
                credentials: 'same-origin'
            }).then(res => res.json()).catch(console.log);
        };

        const avatar_panel = document.querySelector('.avatar_panel');
        const avatar = document.querySelector('.account');

        const nickname = document.querySelector('.nickname');
        let user_data = get_user_info_obj();
        user_data.then(user_obj => {
            if (user_obj['flag']) {
                nickname.textContent = user_obj['dataObj'][1]['nickname'];
            } else { // fallback
                nickname.textContent = '噢，名字走丢了';
            }
        }).catch(console.log);

        const balance = document.querySelector('.balance');

        let balance_data = get_user_balance_obj();
        balance_data.then(balance_obj => {
            if (balance_obj['flag']) {
                balance.textContent = balance_obj['dataObj']['balance'];
            } else { // fallback
                balance.textContent = '噢，金子走丢了';
            }
        }).catch(console.log);

        // TODO: fix hover on avatar
        avatar.addEventListener('mouseenter', () => {
            avatar.classList.add('hide');
            avatar_panel.classList.add('show');
        });

        avatar_panel.addEventListener('mouseleave', e => {
            e.stopPropagation();
            avatar.classList.remove('hide');
            avatar_panel.classList.remove('show');
        });
    }

    function setLogoutBtn() {
        const logout_btn = document.querySelector('.logout');

        let sendLogoutRequest = () => {
            const requestURL = '/user/exit';

            return fetch(requestURL, {
                method: "POST",
                credentials: "same-origin"
            })
                .then(res => res.json())
                .catch(console.log);
        };

        logout_btn.addEventListener('click', e => {
            e.preventDefault();
            sendLogoutRequest().then(obj => {
                if (obj['flag']) {
                    location.reload();
                } else {
                    console.log('something went wrong');
                    console.log(obj);
                }
            }).catch(console.log);
        });
    }

    const login_register = document.querySelector('.login_and_register');
    const with_avatar = document.querySelector('.with_avatar');
    checkIfLogon().then(flag => {
        if (flag) {
            swap_img().then(() => {
                login_register.classList.add('hidden');
                with_avatar.classList.remove('hidden');
                setLogoutBtn();
                avatar_panel_handler();
            });
        } else {
            login_register.classList.remove('hidden');
            with_avatar.classList.add('hidden');
        }
    }).catch(console.log);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        accountHandler();
    });
} else {
    accountHandler();
}