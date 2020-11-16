function autoCompleteUserInfo() {
    function fetchUserInfo() {
        const requestURL = '/order/preShowOrder';

        return fetch(requestURL, {
            method: "POST",
            credentials: "same-origin"
        }).then(res => res.json()).catch(console.log);
    }

    function setUserInfo({ [0]: user, [1]: userInfo }) {
        let name_input = document.getElementById('name');
        let phone_input = document.getElementById('phone');
        let section_input = document.getElementById('section');
        let building_input = document.getElementById('building');

        name_input.value = user['nickname'] || '';
        phone_input.value = userInfo['phone'] || '';
        let address_array = (userInfo['address'] || '').split('#');
        section_input.value = address_array[0];
        building_input.value = address_array[1];
    }

    let user_data = fetchUserInfo();
    user_data.then(data_obj => {
        if (data_obj['dataObj']) {
            setUserInfo(data_obj['dataObj']);
        } else {
            console.log('user info is empty');
        }
    }).catch(console.log);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        autoCompleteUserInfo();
    });
} else {
    autoCompleteUserInfo();
}