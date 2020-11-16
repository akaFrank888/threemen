"use strict";
function slider() {
    const slider_items = document.querySelectorAll('.slider_item');
    const IMG_LIST_LEN = slider_items.length;
    let index = 0;

    let setActive = (i) => {
        for (let item of slider_items) {
            item.classList.remove('show');
        }

        slider_items[i].classList.add('show');
    };
    index = index % IMG_LIST_LEN;
    setActive(index);
    index++;
    setInterval(() => {
        index = index % IMG_LIST_LEN;
        setActive(index);
        index++;
    }, 5000);
}

window.addEventListener('load', slider);