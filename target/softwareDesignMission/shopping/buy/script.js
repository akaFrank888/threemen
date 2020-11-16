"use strict";

function siteCustomHandler() {
    const list = document.querySelector('.site_custom_choice_wrapper');
    const specific_address = document.querySelector('.info_item.site_choice');

    list.addEventListener('click', e => {
        if (e.target.tagName === 'INPUT') {
            switch (e.target.id) {
                case 'specific_address':
                    specific_address.classList.remove('hide');
                    specific_address.classList.add('show');
                    break;
                case 'nearby':
                    specific_address.classList.add('hide');
                    specific_address.classList.remove('show');
                    break;
                default:
                    console.log(`error handling param ${e.target.value}`);
                    break;
            }
        }
    });
    specific_address.classList.add('show');
}

function suggestionHandler() {
    const list = document.querySelector('.suggestion_list');
    const textarea = document.getElementById('note');

    list.addEventListener('click', e => {
        if (e.target.tagName === 'BUTTON') {
            textarea.value += e.target.textContent + " ";
        }
    });
}

function sendOrderHandler() {
    function makeRequest() {
        const requestURL = '/order/saveOrder';
        // const estimatedPriceInput = document.getElementById('estimated_price');
    
        const form_self = document.querySelector('form');
    
        let form_data = new FormData(form_self);
        let urlParams = new URLSearchParams();
    
        form_data.forEach((value, key) => {
            urlParams.append(key, value);
        });
        
        // delete radio group value
        if (urlParams.get('site_custom_choice') === '就近购买') {
            urlParams.set('commAddress', '就近购买');
        }
        urlParams.delete('site_custom_choice');

    
        // add estimated gold
        urlParams.append('commCostCoin', 30); // buy default 20 
    
        let getURL = new URLSearchParams([['serviceType', 'marketService']]);
        return fetch(requestURL + '?' + getURL.toString(), {
            method: 'POST',
            body: urlParams,
            credentials: 'same-origin'
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

    document.querySelector('.checkin_btn').addEventListener('click', e => {
        e.preventDefault();
        makeRequest().then(data => {
            if (data['flag']) {
                alert('订单已发送！');
            } else {
                alert('很抱歉，出了一些问题');
            }
            
            return data['dataObj'];
        })
        .then(num => {
            return payOrder(20, num);
        })
            .then(data => {
                if (data['flag']) {
                    location.href = '/me/order';
                } else {
                    console.log(data);
                }
            })
            .catch(console.log);
    });
}

// function estimatedGold() {
//     const estimatedPriceInput = document.getElementById('estimated_price');
//     const total_content = document.querySelector('.total_content');

//     estimatedPriceInput.addEventListener('blur', () => {
//         if (estimatedPriceInput.value !== '') {
//             total_content.textContent = parseInt(estimatedPriceInput.value) * 10;
//         } else {
//             total_content.textContent = 0;
//         }
//     });
// }

let on_loaded = () => {
    siteCustomHandler();
    suggestionHandler();
    // estimatedGold();
    sendOrderHandler();
};

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', on_loaded);
} else {
    on_loaded();
}
