"use strict";

function pageBehaviorHandler() {
    function hover_main_animate() {
        let on_hover = e => {
            const item_img = e.target.parentElement.children[1].children[0];
            const text_content = e.target.parentElement.children[2];
            const item_title = e.target.parentElement.children[2].children[0];
            const item_description = e.target.parentElement.children[2].children[1];
            switch (e.type) {
                case 'mouseenter':
                    item_img.style.transform = 'scale(1.2)';
                    text_content.style.backgroundColor = 'rgba(0, 0, 0, 0.3)';
                    item_title.style.transform = 'translate(0)';
                    item_description.style.transform = 'translate(0)';
                    break;
                case 'mouseout':
                    text_content.removeAttribute('style');
                    item_img.removeAttribute('style');
                    item_title.removeAttribute('style');
                    item_description.removeAttribute('style');
                    break;
                default:
                    throw `invalid parameter ${e.type}`;
            }
        };

        const content_wrapper = document.querySelector('.content_wrapper');
        // e.target 应该是 a.cover
        content_wrapper.addEventListener('mouseenter', e => {
            e.stopPropagation();
            if (e.target.classList.contains('cover')) {
                on_hover(e);
            }
        }, { capture: true }); // 捕获事件,非冒泡
        content_wrapper.addEventListener('mouseout', e => {
            if (e.target.classList.contains('cover')) {
                on_hover(e);
            }
        });
    }

    function pageSwitchHandler() {
        function listenListHandler() {
            const pos_list = document.querySelector('.position_selection_list');
            const floor_list = document.querySelector('.floor_selection_list');

            function activeListItem(item_link, list) {
                const active_token = 'current_active';
                const resetListItem = (...rest) => {
                    rest.forEach(list => {
                        for (const li of list.children) {
                            li.classList.remove(active_token);
                        }
                    });
                };
                resetListItem(list);
                const link_parent = item_link.parentElement;
                link_parent.classList.add(active_token);
            }
            function controlListButton(catagory, action) {
                const list_items = document.querySelectorAll(`.floor_selection_list .list_item[data-position=${catagory}]`);
                switch (action) {
                    case 'show': {
                        list_items.forEach(item => item.classList.remove('hide'));
                        break;
                    }
                    case 'hide': {
                        list_items.forEach(item => item.classList.add('hide'));
                        break;
                    }
                    default:
                        throw `unexpected param ${action}`;
                }
            }

            // 控制内容显示
            let updateContentList = (position, floor) => {
                let items = document.querySelectorAll('.content_list .list_item');

                // 点击层数列表，floor不为undefined
                if (floor !== undefined) {
                    if (position !== 'all') {
                        for (let item of items) {
                            if (item.dataset.position !== position || item.dataset.floor !== floor) {
                                item.classList.add('hide');
                            } else {
                                item.classList.remove('hide');
                            }
                        }
                    } else {
                        for (let item of items) {
                            item.classList.remove('hide');
                        }
                    }
                } else { // 点击位置列表，floor不为undefined
                    if (position !== 'all') {
                        for (let item of items) {
                            if (item.dataset.position !== position) {
                                item.classList.add('hide');
                            } else {
                                item.classList.remove('hide');
                            }
                        }
                    } else {
                        for (let item of items) {
                            item.classList.remove('hide');
                        }
                    }
                }

            };

            let changeFloorList = (position) => {
                switch (position) {
                    case '南苑食堂':
                        controlListButton('南苑食堂', 'show');
                        controlListButton('北苑食堂', 'hide');
                        break;
                    case '北苑食堂':
                        controlListButton('北苑食堂', 'show');
                        controlListButton('南苑食堂', 'hide');
                        break;
                    case 'all':
                        controlListButton('北苑食堂', 'show');
                        controlListButton('南苑食堂', 'show');
                        break;
                    default:
                        console.log(`unexpected param ${position}`);
                        break;
                }
            };

            const all = document.querySelector('.list_item.all .item_link');

            let listenList = (list) => {
                list.addEventListener('click', e => {
                    if (e.target.tagName === 'A') {
                        e.preventDefault();
                        activeListItem(e.target, list); // 控制列表样式
                        if (list === pos_list) {
                            activeListItem(all, all.parentElement.parentElement);
                            changeFloorList(e.target.parentElement.dataset.position); // 控制楼层列表项显示
                            updateContentList(e.target.parentElement.dataset.position); // 控制内容显示
                        } else {
                            updateContentList(e.target.parentElement.dataset.position, e.target.parentElement.dataset.floor);
                        }
                    }
                });
            };

            listenList(pos_list);
            listenList(floor_list);
        }

        function getStallDishes(shop_name, floor) {
            function makeRequest(shop_name) {
                const requestURL = '/restaurantInfo/showDishByShop';

                let urlParams = new URLSearchParams();

                urlParams.append('shopName', shop_name);

                return fetch(requestURL, {
                    method: 'POST',
                    body: urlParams,
                    credentials: 'same-origin'
                }).then(res => res.json()).catch(console.log);
            }

            function cleanList() {
                const dishes_list = document.querySelector('.dishes_list');

                while (dishes_list.childElementCount > 1) {
                    dishes_list.removeChild(dishes_list.firstElementChild.nextElementSibling);
                }
            }

            // expected base64 string
            function createListItem(dishes_name, dishes_price, dishes_img_src, dishes_id, floor) {
                let list_item = document.createElement('dd');
                list_item.classList.add('dishes_item');

                let img_wrapper = document.createElement('div');
                img_wrapper.classList.add('dishes_img_wrapper');

                let dishes_img = new Image();
                dishes_img.classList.add('dishes_img');
                dishes_img.src = 'data:image/png;base64,' + dishes_img_src;

                img_wrapper.append(dishes_img);

                let desc = document.createElement('div');
                desc.classList.add('dishes_desc');

                let name = document.createElement('h1');
                name.classList.add('dishes_name');
                name.textContent = dishes_name;

                let price = document.createElement('p');
                price.classList.add('dishes_price');

                let price_content = document.createElement('span');
                price_content.classList.add('price_content');
                price_content.textContent = dishes_price;

                price.append(price_content);

                desc.append(name, price);

                list_item.append(img_wrapper, desc);

                list_item.dataset.name = dishes_name;
                list_item.dataset.price = dishes_price;
                list_item.dataset.dishesid = dishes_id;
                list_item.dataset.floor = floor;

                list_item.innerHTML += ` <div class="dishes_add">
                <button type="button" class="remove_dishes dishes_btn invisible">-</button>
                <span class="counter invisible">0</span>
                <button type="button" class="add_dishes dishes_btn">+</button>
                </div>`;

                return list_item;
            }

            function updateDishesList(shop_name, floor) {
                const dishes_list = document.querySelector('.dishes_list');

                cleanList();

                return makeRequest(shop_name).then(data_obj => {
                    let dishes_array = data_obj['dataObj'];

                    dishes_array.forEach(o => {
                        dishes_list.append(createListItem(o['dishName'], o['dishPrice'], o['img'], o['dishId'], floor));
                    });
                }).catch(console.log);
            }

            return updateDishesList(shop_name, floor);
        }

        let dishes = [];

        function setStallInfo(shop_img_base64_with_prefix, position) {
            let setStallAvatar = (shop_img_base64_with_prefix) => {
                const stall_avatar = document.getElementById('stall_avatar');

                stall_avatar.src = shop_img_base64_with_prefix;
            };

            setStallAvatar(shop_img_base64_with_prefix);

            let setPosition = (position) => {
                const position_content = document.getElementById('position');

                position_content.textContent = position;
            };

            setPosition(position);
        }

        function removeAddBtnListner(section, func) {
            section.removeEventListener('click', func);
            dishes = [];
            setPrice(); // reset to 0
        }

        /*
         ** dish = { name: '', dish_id: '', counter: 0, price_per: 0, floor: 1 }
         */

        function addOrRemoveDishes(btn, action) {
            let dishes_name = btn.parentElement.parentElement.dataset.name;
            let dishes_id = btn.parentElement.parentElement.dataset.dishesid;
            let dishes_price_per = Number(btn.parentElement.parentElement.dataset.price);
            let floor = Number(btn.parentElement.parentElement.dataset.floor);
            let index = dishes.findIndex(dish => dish['dish_id'] === dishes_id);

            switch (action) {
                case 'add':
                    if (index !== -1) {
                        dishes[index]['counter']++;
                    } else {
                        dishes.push({ 'name': dishes_name, 'dish_id': dishes_id, 'counter': 1, 'price_per': dishes_price_per, 'floor': floor });
                    }
                    console.log('dishes pushed.');
                    break;
                case 'remove':
                    if (index !== -1) {
                        if (dishes[index]['counter'] === 1) {
                            dishes.splice(index, 1); // remove this dishes
                        } else {
                            dishes[index]['counter']--;
                        }
                        console.log('dishes popped.')
                    } else {
                        throw `illegal action performed. shouldn't be able to remove dishes that doesn't exist.`;
                    }
                    break;
                default:
                    throw `unexpected param ${action}`;
            }
        }

        function setBtnSection(btn, action) {
            let btn_section = btn.parentElement;
            let remove_btn = btn_section.children[0];
            // let add_btn = btn_section.children[2];
            let counter = btn_section.children[1];

            switch (action) {
                case 'add':
                    if (remove_btn.classList.contains('invisible')) {
                        remove_btn.classList.remove('invisible');
                        counter.classList.remove('invisible');
                    }
                    counter.textContent = parseInt(counter.textContent) + 1;
                    break;
                case 'remove':
                    if (parseInt(counter.textContent) === 1) {
                        counter.classList.add('invisible');
                        remove_btn.classList.add('invisible');
                    }
                    counter.textContent = parseInt(counter.textContent) - 1;
                    break;
                default:
                    throw `unexpected param ${action}`;
            }
        }


        /*
         ** <button type="button" class="remove_dishes dishes_btn invisible">-</button>
         ** <span class="counter invisible">1</span>
         ** <button type="button" class="add_dishes dishes_btn">+</button>
         */
        function clickBtnHandler(e) {
            if (e.target.tagName === 'BUTTON') {
                if (e.target.classList.contains('remove_dishes')) {
                    addOrRemoveDishes(e.target, 'remove');
                    setBtnSection(e.target, 'remove');
                } else {
                    addOrRemoveDishes(e.target, 'add');
                    setBtnSection(e.target, 'add');
                }
                setPrice();
            }
        }

        function dishesBtnHandler() {
            let dishes_add_sections = document.querySelectorAll('.dishes_add');

            dishes_add_sections.forEach(section => {
                removeAddBtnListner(section, clickBtnHandler);
                section.addEventListener('click', clickBtnHandler);
            });
        }

        function setPrice() {
            const total_content = document.querySelector('.total_content');

            let total = 0;
            dishes.forEach(dish => total += dish['price_per'] * dish['counter']);

            total_content.textContent = total.toFixed(1);
        }


        function listenItemHandler() {
            const content_list = document.querySelector('.content_list');

            content_list.addEventListener('click', e => {
                if (e.target.tagName === 'A') {
                    e.preventDefault();
                    let shop_name = e.target.parentElement.children[2].firstElementChild.textContent;
                    let shop_img_base64_with_prefix = e.target.parentElement.children[1].firstElementChild.src;
                    let floor = e.target.parentElement.dataset.floor;
                    console.log(floor);
                    getStallDishes(shop_name, floor)
                        .then(() => dishesBtnHandler());
                    setStallInfo(shop_img_base64_with_prefix, e.target.parentElement.dataset.position);
                }
            });
        }

        function stopConfirmPanelBubbling() {
            const panel = document.querySelector('.order_confirmation');

            panel.addEventListener('click', e => {
                e.stopPropagation();
            });
        }

        function updateConfirmPanelList(dishes_o_array) {
            function createListItem(dishes_o) {
                let list_item = document.createElement('li');

                list_item.classList.add('list_item');

                let name = document.createElement('span');
                name.classList.add('order_content');
                name.textContent = dishes_o['name'];

                let counter = document.createElement('span');
                counter.classList.add('order_number');
                counter.textContent = dishes_o['counter'];

                let total_price = document.createElement('span');
                total_price.classList.add('order_reward');
                total_price.textContent = (dishes_o['counter'] * dishes_o['price_per']).toFixed(1);

                list_item.append(name, counter, total_price);

                return list_item;
            }

            const list = document.querySelector('.order_list');

            function cleanList() {
                while (list.firstChild) {
                    list.removeChild(list.firstChild);
                }
            }

            function updateTotal(dishes_o_array) {
                const total_content = document.getElementById('counter')

                let total = 0;
                dishes_o_array.forEach(o => {
                    total += o['counter'] * o['price_per'];
                });

                total_content.textContent = total.toFixed(1);
            }

            cleanList();

            dishes_o_array.forEach(o => {
                list.append(createListItem(o));
            });

            updateTotal(dishes_o_array);
        }

        function popUpConfirmPanelHandler(dishes_o_array) {
            const panel = document.querySelector('.order_confirmation');

            // show panel
            panel.classList.remove('hide');

            updateConfirmPanelList(dishes_o_array);
        }

        function listenMakeOrderBtnHandler() {
            const buy_btn = document.querySelector('.buy_btn');

            buy_btn.addEventListener('click', e => {
                e.preventDefault();
                if (dishes[0]) {
                    popUpConfirmPanelHandler(dishes);
                    bindConfirmedSubmitBtn(dishes);
                } else {
                    alert('提交前，请选择菜品');
                }
            });
        }

        let location = {
            1: '北苑1楼',
            2: '北苑2楼',
            3: '北苑3楼',
            4: '南苑4楼',
            5: '南苑5楼'
        };

        function bindConfirmedSubmitBtn(dishes_o_array) {
            function preAddDishesToCart(dishes_o_array) {
                const requestURL = '/restaurantOrder/addToCart';

                let promises = [];
                dishes_o_array.forEach(dish => {
                    for (let i = 0; i < dish['counter']; i++) {
                        let urlParams = new URLSearchParams();

                        urlParams.append('dishId', dish['dish_id']);
                        urlParams.append('location', dish['floor']);

                        promises.push(fetch(requestURL + '?' + urlParams.toString(), {
                            method: 'GET',
                            credentials: 'same-origin'
                        }).then(res => res.json()).catch(console.log));
                    }
                });

                return Promise.all(promises);
            }

            function calcReal(dishes_o_array) {

                let result = 0;
                dishes_o_array.forEach(dish => {
                    result += dish['price_per'] * dish['counter'];
                });

                return result;
            }

            function sendOrderRequest() {
                const requestURL = '/order/saveOrder'; // TODO
                const formSelf = document.querySelector('.order_confirmation form');
                let formData = new FormData(formSelf);

                let urlParams = new URLSearchParams();
                for (let data of formData) {
                    urlParams.append(data[0], data[1]);
                }

                urlParams.append('commAddress', location[parseInt(dishes[0]['floor'])]);
                urlParams.append('commCostCoin', 20);
                urlParams.append('commCostReal', calcReal(dishes).toFixed(1));

                let getParams = new URLSearchParams();
                getParams.append('serviceType', 'restaurantService');
                let getURL = requestURL + '?' + getParams.toString();
                return fetch(getURL, {
                    method: 'POST',
                    body: urlParams,
                    credentials: "same-origin"
                }).then(res => res.json()).catch(console.log);
            }

            function payOrder(gold, order_id) {
                const requestURL = '/order/payOrder';
                let urlParams = new URLSearchParams();
        
                urlParams.append('commCostCoin', gold);
                urlParams.append('commNum', order_id);
                urlParams.append('commReward', 0);
        
                return fetch(requestURL, {
                    method: 'POST',
                    body: urlParams,
                    credentials: 'same-origin'
                }).then(res => res.json()).catch(console.log);
            }

            function onClickBtn(e) {
                e.preventDefault();
                if (dishes[0]) {
                    preAddDishesToCart(dishes_o_array)
                        .then(sendOrderRequest)
                        .then(data_obj => {
                            if (data_obj['flag']) {
                                alert('提交成功');
                            } else {
                                alert('出现了问题');
                                console.log(data_obj);
                            }

                            return data_obj['dataObj'];
                        })
                        .then(id => {
                            return payOrder(20, id);
                        })
                        .then(data_obj => {
                            if (data_obj['flag']) {
                                window.location.href = '/me/order';
                            } else {
                                console.log(data_obj);
                            }
                        })
                        .catch(console.log);
                } else {
                    console.log('无菜品');
                }
            }

            const checkin_btn = document.querySelector('.checkin_btn');

            checkin_btn.removeEventListener('click', onClickBtn);
            checkin_btn.addEventListener('click', onClickBtn);
        }

        function listenCancelBtn() {
            const cancel_btn = document.querySelector('.cancel_btn');
            const panel = document.querySelector('.order_confirmation');

            cancel_btn.addEventListener('click', () => {
                panel.classList.add('hide');
            });
        }


        stopConfirmPanelBubbling();
        listenListHandler();
        listenItemHandler();
        listenMakeOrderBtnHandler();
        listenCancelBtn();
    }

    function stopHeaderTriggerEvent() {
        document.querySelector('header').addEventListener('click', e => {
            e.stopPropagation();
        });
    }

    function asidePanelHandler() {
        const aside = document.querySelector('aside');
        const mask = document.querySelector('.go_dark');

        document.body.addEventListener('click', () => {
            aside.classList.remove('show');
            mask.classList.remove('active');
        });

        // stop click event in aside panel from going up
        aside.addEventListener('click', e => {
            e.stopPropagation();
        });

        const content_list = document.querySelector('.content_list');

        content_list.addEventListener('click', e => {
            if (e.target.classList.contains('cover')) {
                e.stopPropagation();
                aside.classList.add('show');
                mask.classList.add('active');
            }
        });

        let setItemStyle = (item_link, list) => {
            for (let ele of list.children) {
                if (ele.classList.contains('active')) {
                    ele.classList.remove('active');
                }
            }
            item_link.parentElement.classList.add('active');
        };

        // find element in the list with index
        let findItem = (index, list) => {
            for (let ele of list) {
                if (ele.dataset.index === index) {
                    return ele;
                }
            }
        };

        let scrollToSection = (index, list) => {
            let expected_target = findItem(index, list);
            expected_target.scrollIntoView();
        };

        const side_nav = document.querySelector('.side_nav_list');

        const dishes_list_titles = document.querySelectorAll('.dishes_section_title');

        side_nav.addEventListener('click', e => {
            if (e.target.tagName === 'A') {
                e.preventDefault();
                console.log(e.target.dataset.index);
                setItemStyle(e.target, side_nav);
                scrollToSection(e.target.dataset.index, dishes_list_titles);
            }
        });

        let switch_panel = (panel) => {
            const content = document.querySelector('.stall_content_wrapper');
            const info = document.querySelector('.stall_info_wrapper');

            switch (panel) {
                case 'order':
                    content.classList.remove('hide');
                    info.classList.add('hide');
                    break;
                case 'info':
                    info.classList.remove('hide');
                    content.classList.add('hide');
                    break;
                default:
                    throw `unexpected param ${panel}`;
            }
        };

        const tab_list = document.querySelector('.tab_list');

        tab_list.addEventListener('click', e => {
            if (e.target.tagName === 'A') {
                setItemStyle(e.target, tab_list);
                switch_panel(e.target.parentElement.dataset.panel);
            }
        });
    }

    pageSwitchHandler();
    hover_main_animate();
    stopHeaderTriggerEvent();
    asidePanelHandler();
}

function updateListHandler() {
    const location_list = {
        1: '北苑食堂',
        2: '北苑食堂',
        3: '北苑食堂',
        4: '南苑食堂',
        5: '南苑食堂'
    };

    function makeRequest(location_id) {
        const requestURL = '/restaurantInfo/showShopsByLocation';
        // const stallData = '/static/json/data.json';

        let urlParams = new URLSearchParams();

        urlParams.append('location_id', location_id);

        let getURL = requestURL + '?' + urlParams.toString();
        return fetch(getURL, {
            method: 'GET',
            credentials: 'same-origin'
        }).then(res => res.json()).catch(console.log);
    }

    // img expected base64 string
    function createListItem(stall_img_src, stall_name, stall_desc, stall_position, stall_floor) {
        let cover = document.createElement('a');
        cover.classList.add('cover');

        let bg_block = document.createElement('div');
        bg_block.classList.add('bg_block');

        let bg_img = new Image();
        bg_img.classList.add('bg_img');
        bg_img.src = 'data:image/png;base64,' + stall_img_src;
        bg_img.alt = stall_name;

        bg_block.append(bg_img);

        let text_content = document.createElement('div');
        text_content.classList.add('text_content');

        let item_title = document.createElement('h1');
        item_title.classList.add('item_title');
        item_title.textContent = stall_name;

        let desc = document.createElement('p');
        desc.classList.add('description');
        desc.textContent = stall_desc || '10:30-13:00; 17:00-19:00';

        text_content.append(item_title, desc);

        let list_item = document.createElement('li');
        list_item.classList.add('list_item');
        list_item.dataset.position = stall_position;
        list_item.dataset.floor = stall_floor;

        list_item.append(cover, bg_block, text_content);

        return list_item;
    }

    function fillStallList() {
        const content_list = document.querySelector('.content_list');

        const keys = Object.keys(location_list);

        let promises = [];
        keys.forEach((value) => {
            promises.push(
                makeRequest(value).then(res_obj => {
                    let data_obj = res_obj;
                    let info_array = data_obj['dataObj'];
                    // expected item structure: info_sub_array[0]: shop_name, info_sub_array[1]: img in base64
                    info_array.forEach(info_sub_array => {
                        content_list.append(createListItem(info_sub_array[1], info_sub_array[0], '', location_list[value], value));
                    });
                }).catch(console.log)
            );
        });

        return Promise.all(promises);
    }

    fillStallList().then(() => {
        pageBehaviorHandler();
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        updateListHandler();
    });
} else {
    updateListHandler();
}