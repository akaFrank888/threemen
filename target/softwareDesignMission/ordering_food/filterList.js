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
}
