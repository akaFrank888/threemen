function setPageBtn(currentPage: number): void {
    // helper to find out if is swap page button
    function isArrowButton(list_item: HTMLLIElement): boolean {
        if (list_item.classList.contains('prev_page') || list_item.classList.contains('next_page')) {
            return true;
        } else {
            return false;
        }
    }
    const seletion_page = document.querySelector('.select_page_list') as HTMLUListElement;

    for (const li of seletion_page.children) {
        // first element child is button
        if (!isArrowButton(li as HTMLLIElement) && (parseInt((li.firstElementChild as HTMLButtonElement).dataset.page) === currentPage)) {
            li.firstElementChild.classList.add('active');
        }
    }
}