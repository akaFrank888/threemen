function changeUserInfoHandler(): void {
    const change_pw_panel = document.querySelector('.change_pw_panel') as HTMLDivElement;

    change_pw_panel.addEventListener('click', (e: Event) => {
        e.stopPropagation();
    });

    const change_pw_form = document.getElementById('change_pw') as HTMLFormElement;

    function makeRequest(): Promise<Record<string, unknown>> {
        const requestURL = '/userInfo/saveUserInfo';

        const form_data = new FormData(change_pw_form);
        const urlParams = new URLSearchParams();
        for (const data of form_data) {
            urlParams.append(data[0], (data[1] as string));
        }

        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    change_pw_form.addEventListener('submit', (e: Event) => {
        e.preventDefault();
        makeRequest().then((obj: Record<string, unknown>) => {
            if (obj['flag']) {
                alert(obj['errorMsg']);
                window.location.replace('/me/');
            } else {
                alert(obj['errorMsg']);
            }
        }).catch(console.log);
    });
}

function verificationHandler(): void {
    const formSelf = document.getElementById('verify_form') as HTMLFormElement;
    function makeRequest(): Promise<Record<string, unknown>> {
        const requestURL = '/userInfo/userRealize';


        const formData = new FormData(formSelf);
        const urlParams = new URLSearchParams();
        for (const data of formData) {
            urlParams.append(data[0], (data[1] as string));
        }

        return fetch(requestURL, {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    formSelf.addEventListener('submit', (e: Event) => {
        e.preventDefault();
        makeRequest().then((obj: Record<string, unknown>) => {
            if (obj['flag']) {
                alert('认证通过，请继续填写个人信息');
                hideVerifyAndShowInfoPanel();
                history.replaceState(null, '', window.location.pathname);
            } else {
                alert('认证失败，请重试。' + obj['errorMsg']);
            }
        }).catch(console.log);
    });
}

function hideVerifyAndShowInfoPanel(): void {
    const verify_panel = document.querySelector('.verify_panel') as HTMLDivElement;
    const edit_panel = document.querySelector('.change_pw_panel') as HTMLDivElement;

    verify_panel.classList.add('hide');
    edit_panel.classList.remove('hide');
}
// check if redirected from registeration page
// if true, hide edit panel and show verify panel
// else hide verify_panel and show edit panel
// by manipulating class 'hide'
// class hide is in html by default
function checkIfIsFromRegisterationPage(): void {
    const verify_panel = document.querySelector('.verify_panel') as HTMLDivElement;
    const edit_panel = document.querySelector('.change_pw_panel') as HTMLDivElement;

    if (window.location.hash.substring(1) === 'register') {
        verify_panel.classList.remove('hide');
        verificationHandler();
    } else {
        edit_panel.classList.remove('hide');
    }
    
    changeUserInfoHandler();
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', checkIfIsFromRegisterationPage);
} else {
    checkIfIsFromRegisterationPage();
}