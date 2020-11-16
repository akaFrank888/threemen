"use strict";

function toTop() {
    let to_top_btn = document.querySelector('.to_top');
    to_top_btn.addEventListener('click', () => window.scroll({ top: 0, behavior: 'smooth' }));
}

function goBack() {
    let go_back_btn = document.querySelector('.go_back');
    go_back_btn.addEventListener('click', () => history.back());
}

window.addEventListener('load', () => {
    // filterHandler();
    toTop();
    goBack();
});