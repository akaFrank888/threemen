"use strict";

// 获取指定信息并修改DOM
function setUserInfo() {
    function getUserInfo() {
        const requestURL = '/userInfo/showUserInfo';

        return fetch(requestURL, {
            method: "POST",
            credentials: "same-origin"
        }).then(res => res.json()).catch(console.log);
    }

    function checkIfVerified() {
        const requestURL = '/workerInfo/isRealize';

        return fetch(requestURL, {
            method: 'GET',
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    function getWorkerInfo() {
        const requestURL = '/workerInfo/showInfo';

        return fetch(requestURL, {
            method: 'POST',
            credentials: 'same-origin',
        }).then(res => res.json()).catch(console.log);
    }

    function getUserAvatar() {
        const requestURL = '/userInfo/showImg';

        return fetch(requestURL, {
            method: 'POST',
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    getUserInfo().then(data => {
        const user_contact_o = data['dataObj'][0];
        const user_account_o = data['dataObj'][1];
        const doms = {
            nickname: [document.getElementById('nickname_avatar'), document.getElementById('nickname_detail')],
            phone: document.getElementById('phone'),
            account: document.getElementById('account'),
            email: document.getElementById('email'),
            address: document.getElementById('address')
        };

        const keys = Object.keys(doms);
        keys.forEach((value) => {
            if (Array.isArray(doms[value])) {
                doms[value].forEach((ele) => ele.textContent = user_account_o[value]);
            } else {
                switch (value) {
                    case 'phone':
                        if (user_contact_o) {
                            doms[value].textContent = user_contact_o[value];
                        } else {
                            doms[value].textContent = '';
                        }
                        break;
                    case 'address':
                        if (user_contact_o) { // expected user_contact_o['address'] structure = '南#11'
                            let address_array = user_contact_o[value].split('#');
                            doms[value].textContent = address_array[0] + '苑' + address_array[1] + '栋';
                        } else {
                            doms[value].textContent = '';
                        }
                        break;
                    case 'account':
                    case 'email':
                        doms[value].textContent = user_account_o[value];
                        break;
                    default:
                        console.log(`unexpected value ${value}`);
                        break;
                }
            }
        });
    }).catch(console.log);

    checkIfVerified().then(data => {
        const not_verified = document.querySelector('.not_verified');
        const verified_info_wrapper = document.querySelector('.verification_info_list');
        if (data['flag']) {
            getWorkerInfo().then(data => {
                const worker_obj = data['dataObj'];

                const doms = {
                    stuId: document.getElementById('id'),
                    realName: document.getElementById('real_name')
                };

                const keys = Object.keys(doms);
                keys.forEach((value) => {
                    doms[value].textContent = worker_obj[value];
                });
            }).catch(console.log);
            not_verified.classList.add('hide');
            verified_info_wrapper.classList.remove('hide');
        } else {
            not_verified.classList.remove('hide');
            verified_info_wrapper.classList.add('hide');
        }
    }).catch(console.log);

    getUserAvatar().then(data => {
        const avatar = document.getElementById('my_avatar');

        if (data['dataObj']) {
            avatar.src = 'data:image/png;base64,' + data['dataObj'];
        } else {
            avatar.src = '/static/img/default_avatar.png';
        }
    }).catch(console.log);
}

// 进行信息的获取和DOM修改
function userInfoHandler() {
    setUserInfo();
}

function uploadAvatarHandler() {
    function listenUploadImg() {
        const preview = document.getElementById('preview');
        const input = document.getElementById('upload');

        input.addEventListener('change', () => {
            preview.src = URL.createObjectURL(input.files[0]);
        });
    }

    listenUploadImg();

    function uploadAvatar() {
        const form_self = document.querySelector('.upload_form');
        form_self.addEventListener('submit', e => {
            e.preventDefault();

            const uploadURL = '/userInfo/saveImg';
            let form_data = new FormData(form_self);
            if (form_data.get('upload') !== '') {
                return fetch(uploadURL, {
                    method: 'POST',
                    body: form_data,
                    credentials: 'same-origin'
                })
                    .then(res => res.json())
                    .then(obj => {
                        if (obj['flag']) {
                            alert('上传成功！');
                            location.replace(location.href);
                        } else {
                            alert('发生错误');
                        }
                    })
                    .catch(console.log);
            } else {
                alert('没有选择图片！');
            }
        });

        const change_img_link = document.querySelector('.mask');
        const chanage_img_panel = document.querySelector('.upload_avatar');

        change_img_link.addEventListener('click', e => {
            e.preventDefault();
            chanage_img_panel.classList.toggle('show');
        });

        chanage_img_panel.addEventListener('click', e => {
            e.stopPropagation();
        })

        document.body.addEventListener('click', e => {
            if (e.target !== change_img_link && chanage_img_panel.classList.contains('show')) {
                chanage_img_panel.classList.remove('show');
            }
        });
    }

    uploadAvatar();
}

function changePasswordHandler() {
    const show_change_btn = document.querySelector('.reset_passwd');
    const change_pw_panel = document.querySelector('.change_pw_panel');
    show_change_btn.addEventListener('click', () => {
        change_pw_panel.classList.toggle('show');
    });

    change_pw_panel.addEventListener('click', e => {
        e.stopPropagation();
    });

    document.body.addEventListener('click', e => {
        if (e.target !== show_change_btn && change_pw_panel.classList.contains('show')) {
            change_pw_panel.classList.remove('show');
        }
    });

    const change_pw_form = document.getElementById('change_pw');
    const new_pw_input = document.getElementById('new');
    const new_pw_repeat_input = document.getElementById('new_repeat');

    function makeRequest() {
        const requestURL = '/userInfo/updatePassword';

        let form_data = new FormData(change_pw_form);
        let urlParams = new URLSearchParams();
        for (let data of form_data) {
            if (data[0] === 'oldPassword' || data[0] === 'newPassword') {
                urlParams.append(data[0], data[1]);
            }
        }

        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    change_pw_form.addEventListener('submit', e => {
        e.preventDefault();
        if (new_pw_input.value !== new_pw_repeat_input.value) {
            alert('两次输入的密码不相同！');
        } else {
            makeRequest().then(obj => {
                if (obj['flag']) {
                    alert(obj['errorMsg']);
                    location.replace(location.pathname);
                } else {
                    alert(obj['errorMsg']);
                }
            }).catch(console.log);
        }
    });
}

function verificationHandler() {
    function makeRequest() {
        const requestURL = '/workerInfo/workerRealize';

        const form_self = document.querySelector('.verify_form');

        const form_data = new FormData(form_self);

        let urlParams = new URLSearchParams();
        form_data.forEach((value, key) => {
            urlParams.append(key, value);
        });

        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    function sendRequestHandler() {
        const verify_link = document.getElementById('verify_link');
        const verification_panel = document.querySelector('.verify_wrapper');

        verify_link.addEventListener('click', e => {
            e.preventDefault();
            verification_panel.classList.toggle('show');
        });

        verification_panel.addEventListener('click', e => {
            e.stopPropagation();
        });

        document.body.addEventListener('click', e => {
            if (e.target !== verify_link && verification_panel.classList.contains('show')) {
                verification_panel.classList.remove('show');
            }
        });

        const form_self = document.querySelector('.verify_form');

        form_self.addEventListener('submit', e => {
            e.preventDefault();
            makeRequest().then(data => {
                if (data['flag']) {
                    alert('认证成功');
                    location.replace('/me');
                } else {
                    alert('出现了错误！');
                    console.log(data['errorMsg']);
                }
            }).catch(console.log);
        });
    }

    sendRequestHandler();
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        userInfoHandler();
        uploadAvatarHandler();
        changePasswordHandler();
        verificationHandler();
    });
} else {
    userInfoHandler();
    uploadAvatarHandler();
    changePasswordHandler();
    verificationHandler();
}

