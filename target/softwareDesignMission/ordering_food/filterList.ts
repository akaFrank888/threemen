function listenListHandler(): void {
    const pos_list = document.querySelector('.position_selection_list') as HTMLUListElement;
    const floor_list = document.querySelector('.floor_selection_list') as HTMLUListElement;

    function activeListItem(item_link: HTMLAnchorElement, list: HTMLUListElement): void {
        const active_token = 'current_active';

        const resetListItem = (...rest: HTMLUListElement[]): void => {
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

    function controlListButton(catagory: string, action: string) {
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