"use strict";
function pageBehaviorHandler() {
    function sliderHandler() {
        const slider_list = document.querySelector('.slider_list');
        const list_item = document.querySelector('.slider_list .list_item');

        let list_item_width = parseInt(getComputedStyle(list_item).width);
        let pixel = 0;
        let multiplier = 0;
        let changePos = () => {
            list_item_width = parseInt(getComputedStyle(list_item).width);
            if (pixel <= list_item_width * -2 && multiplier >= 2) {
                pixel = 0;
                multiplier = 0;
            } else {
                pixel -= list_item_width;
                multiplier++;
            }
            slider_list.style.transform = `translate(calc(-100% * ${multiplier} / 3))`;
        };

        let timer;
        let interval = () => {
            timer = setInterval(changePos, 3000);
        };
        interval();

        let timeout;
        let pause = () => {
            clearInterval(timer);
            clearTimeout(timeout);
        };

        let resume = () => {
            timeout = setTimeout(() => {
                interval();
            }, 500);
        };

        const wrapper = document.querySelector('.slider_wrapper');
        wrapper.addEventListener('mouseenter', pause);
        wrapper.addEventListener('mouseleave', resume);

        let go_back = () => {
            list_item_width = parseInt(getComputedStyle(list_item).width);
            if (pixel < 0 && multiplier <= 2) {
                pixel += list_item_width;
                multiplier--;
            } else {
                pixel = list_item_width * -2;
                multiplier = 2;
            }
            slider_list.style.transform = `translate(calc(-100% * ${multiplier} / 3))`;
        };

        wrapper.addEventListener('click', e => {
            if (e.target.tagName === 'BUTTON') {
                switch (e.target.className) {
                    case 'prev slider_btn':
                        go_back();
                        break;
                    case 'next slider_btn':
                        changePos();
                        break;
                    default:
                        console.log('invalid btn clicked');
                        break;
                }
            }
        });

        let ticking = false;
        window.addEventListener('resize', () => {
            if (!ticking) {
                window.requestAnimationFrame(() => {
                    pause();
                    resume();
                    ticking = false;
                });

                ticking = true;
            }
        });
    }

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

    function hover_nav_handler() {
        const nav = document.querySelector('.aside_info');
        const header = document.querySelector('header');
        let nav_offset = nav.offsetTop;

        let change_nav_style = (scroll_pos) => {
            if (scroll_pos > nav_offset - parseInt(window.getComputedStyle(header)['height'])) {
                // nav.style.willChange = 'position, top, right';
                nav.classList.add('fixed');
                nav.style.right = nav.parentElement.offsetLeft + 'px';
            } else {
                nav.classList.remove('fixed');
                nav.removeAttribute('style');
            }
        };

        let last_known_scroll_position = 0;
        let ticking = false;
        let change_by_animation = () => {
            last_known_scroll_position = window.scrollY;

            if (!ticking) {
                window.requestAnimationFrame(function () {
                    change_nav_style(last_known_scroll_position);
                    ticking = false;
                });

                ticking = true;
            }
        };
        window.addEventListener('resize', change_by_animation)
        window.addEventListener('scroll', change_by_animation);
    }

    // can be rewritten using scrollIntoView()
    // function jump_to_section_handler() {
    //     const sections = document.querySelectorAll('.section');
    //     const header = document.querySelector('header');

    //     const nav_items = document.querySelectorAll('.aside_list .list_item');
    //     sections.forEach((ele, i) => {
    //         nav_items[i].dataset.pos = ele.offsetTop - parseInt(window.getComputedStyle(header)['height']);
    //     });

    //     const content_wrapper = document.querySelector('.content_wrapper');
    //     let data_top;
    //     // e.target 应是 aside_list 中元素的 a.cover
    //     let clicked = e => {
    //         if (e.target.parentElement.parentElement.classList.contains('aside_list')) {
    //             e.preventDefault();
    //             data_top = e.target.parentElement.dataset.pos;
    //             scrollTo({
    //                 top: data_top,
    //                 behavior: 'smooth'
    //             });
    //         }
    //     };

    //     content_wrapper.addEventListener('click', clicked);
    // }

    sliderHandler();
    hover_main_animate();
    hover_nav_handler();
    // jump_to_section_handler();
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        pageBehaviorHandler();
    });
} else {
    pageBehaviorHandler();
}