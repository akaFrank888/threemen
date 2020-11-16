function setPageBtn(currentPage) {
    // helper to find out if is swap page button
    function isArrowButton(list_item) {
        if (list_item.classList.contains('prev_page') || list_item.classList.contains('next_page')) {
            return true;
        }
        else {
            return false;
        }
    }
    var seletion_page = document.querySelector('.select_page_list');
    for (var _i = 0, _a = seletion_page.children; _i < _a.length; _i++) {
        var li = _a[_i];
        // first element child is button
        if (!isArrowButton(li) && (parseInt(li.firstElementChild.dataset.page) === currentPage)) {
            li.firstElementChild.classList.add('active');
        }
    }
}
