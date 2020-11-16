function makeRequest() {
    const requestURL = '/workerInfo/workerRealize';
    
    const form_self = document.querySelector('form');

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
    makeRequest().then(data => {
        if (data['flag']) {
            alert('认证成功');
            location.replace('/me');
        } else {
            alert('出现了错误！');
            console.log(data['errorMsg']);
        }
    })
}

window.addEventListener('load', () => {
    sendRequestHandler();
});